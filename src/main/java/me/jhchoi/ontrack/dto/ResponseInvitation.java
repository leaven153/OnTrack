package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ResponseInvitation {
    private String projectType;
    private Long projectId;
    private Long userId;
    private String userName;
    private String capacity;
    private String invitedAs;  // position에 들어갈 값
    private LocalDate joinedAt;


    public static String tellCapacity(String projectType, String invitedAs){
        if(Objects.equals(projectType, "team")) {
            return switch (invitedAs) {
                case "partner" -> "PD";
                case "admin" -> "D";
                default -> "W";
            };
        } else if(Objects.equals(projectType, "solo-shared")){
            return switch (invitedAs) {
                case "partner" -> "RC";
                default -> "R";
            };
        } else return null;
    }
}
