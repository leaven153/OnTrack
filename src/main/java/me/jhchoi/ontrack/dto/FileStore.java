package me.jhchoi.ontrack.dto;

import me.jhchoi.ontrack.domain.TaskFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    // 파일 저장할 폴더 생성 (프로젝트id/할일id)
    public String makeFolder(Long projectId, Long taskId){
        Path path = Paths.get(String.valueOf(projectId), String.valueOf(taskId));
        File uploadFolderPath = new File(fileDir, String.valueOf(path));
        if(uploadFolderPath.exists() == false) {
            uploadFolderPath.mkdirs();
        }
        return path.toString();
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
    public List<TaskFile> storeFile(List<MultipartFile> multipartFiles, Long projectId, Long taskId, Long memberId) throws IOException {
        if (multipartFiles.isEmpty()){
            return null;
        }
        List<TaskFile> fileList = new ArrayList<>();

        for(MultipartFile file : multipartFiles) {
            if(!file.isEmpty()) {
                String originalFileName = file.getOriginalFilename();
                String storeFileName = createFileName(originalFileName);
                String savePath = String.valueOf(Paths.get(makeFolder(projectId, taskId), storeFileName));
                file.transferTo(new File(savePath));
                fileList.add(TaskFile.builder()
                        .projectId(projectId)
                        .taskId(taskId)
                        .memberId(memberId)
                        .fileOrigName(originalFileName)
                        .fileNewName(storeFileName)
                        .fileType(extractExt(originalFileName))
                        .fileSize(file.getSize())
                        .filePath(makeFolder(projectId, taskId))
                        .build());
            }
        }

        return fileList;
    }
}
