package me.jhchoi.ontrack.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.TaskFile;
import me.jhchoi.ontrack.util.CustomS3Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component @Slf4j
@RequiredArgsConstructor
public class FileStore {
//    @Value("${file.dir}")
//    private String fileDir;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final CustomS3Util s3Util;

    // 파일 저장할 폴더 생성 (프로젝트id/할일id)
    public String makeFolder(Long projectId, Long taskId){
//        Path path = Paths.get(fileDir, String.valueOf(projectId), String.valueOf(taskId));
        Path path = Paths.get(bucket, String.valueOf(projectId), String.valueOf(taskId));

        File uploadFolderPath = new File(String.valueOf(path));

        if(!uploadFolderPath.exists()) {
            uploadFolderPath.mkdirs();
            log.info("폴더생성: {}", uploadFolderPath.exists());
        }
        return path.toAbsolutePath().toString();
    }


    // 확장자 추출
    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        return originalFilename.substring(idx+1);
    }

    // 저장할 파일 이름 생성 (uuid + 확장자)
    private String createFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 저장 (후 TaskFile entity로 전환하여 반환)
    public List<TaskFile> storeFile(List<MultipartFile> multipartFiles, Long projectId, Long taskId, Long memberId, LocalDateTime createdAt) throws IOException {
        if (multipartFiles.isEmpty()){
            return null;
        }
        List<TaskFile> fileList = new ArrayList<>();

        s3Util.createFolderAWS(projectId, taskId);

        for(MultipartFile file : multipartFiles) {
            if(!file.isEmpty()) {
                int idx = file.getOriginalFilename().lastIndexOf(".");
                String originalFileName = file.getOriginalFilename().substring(0, idx);
                String storeFileName = createFileName(file.getOriginalFilename());
                Path savePath = Paths.get(makeFolder(projectId, taskId), storeFileName);
                log.info("저장경로(폴더, 파일이름): {}", savePath.toAbsolutePath()); // 저장경로(폴더, 파일이름)
                file.transferTo(new File(String.valueOf(savePath))); // 이게.. 저장하는 거였던 가?
                List<Path> pathList = new ArrayList<>();
                pathList.add(savePath);
                s3Util.uploadFiles(pathList); // , true

                fileList.add(TaskFile.builder()
                        .projectId(projectId)
                        .taskId(taskId)
                        .memberId(memberId)
                        .fileOrigName(originalFileName)
                        .fileNewName(storeFileName)
                        .fileType(extractExt(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .filePath(makeFolder(projectId, taskId))
                        .formattedFileSize(fileSizeFormatter(file.getSize()))
                        .createdAt(createdAt)
                        .build());
            }
        }

        return fileList;
    }

    // 파일 사이즈 출력 시 단위 전환하여 출력
    public static String fileSizeFormatter(Long fileSize){

        String result = "";
        if(fileSize < 1048576) {
            float size = fileSize;
            result = String.format("%.1f", size/1024) + "KB";
        } else if(fileSize >= 1048576) {
            result = (int)(fileSize/1048576) + "MB";
        } else if(fileSize > 1073700000) {
            result = (int)(fileSize/1073700000) + "GB";
        }
        return result;
    }
}
