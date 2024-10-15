package me.jhchoi.ontrack.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomS3Util {
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;


    // 폴더 생성
    public void createFolderAWS(Long projectId, Long taskId){
        S3Client client = S3Client.builder().build();
        String folder = String.valueOf(projectId) + "/" + String.valueOf(taskId) + "/";

        // 현재 버킷 내에 있는 폴더 리스트 확인
        ListObjectsRequest request = ListObjectsRequest.builder().bucket(bucket).build();
        ListObjectsResponse response = client.listObjects(request);
        List<S3Object> objects = response.contents();
        log.info("objects list without prefix: ", objects);

        ListIterator<S3Object> listIterator = objects.listIterator();
        while(listIterator.hasNext()){
            S3Object object = listIterator.next();
            log.info("object key: {}", object.key());
        }

        ListObjectsRequest request2 = ListObjectsRequest.builder()
                .bucket(bucket)
                .prefix(folder)
                .build();

        ListObjectsResponse response2 = client.listObjects(request2);

        List<S3Object> objects2 = response2.contents();
        log.info("objects list with prefix: ", objects2);

        ListIterator<S3Object> listIterator1 = objects2.listIterator();
        while(listIterator1.hasNext()){
            S3Object object2 = listIterator1.next();
            log.info("specific folder name: {}", object2.key());
        }

//        return "";
    }
    
    // 파일 업로드
    public void uploadFiles(List<Path> filePaths){ //, boolean delFlag
        if(filePaths == null || filePaths.isEmpty()){
            return;
        }

        for(Path filePath : filePaths){
            PutObjectRequest request = PutObjectRequest.builder()
                                        .bucket(bucket)
                                        .key(filePath.toFile().getName())
                                        .build();

            s3Client.putObject(request, filePath);

//            if(delFlag){
//                try{
//                    Files.delete(filePath);
//                }catch(IOException e){
//                    throw new RuntimeException(e.getMessage());
//                }
//            } // if ends
        } // for ends
    } // uploadFiles ends

    public void deleteFiles(List<Path> filePaths){
        if(filePaths == null || filePaths.isEmpty()){
            return;
        }

        for(Path filePath: filePaths) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(filePath.toFile().getName())
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }
    } // deleteFiles ends
}
