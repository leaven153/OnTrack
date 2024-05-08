package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OnTrackUser {
    private Long id;
    private String userEmail;
    private String userName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime visitedAt;

    @Builder
    public OnTrackUser(String userEmail, String userName, String password, LocalDateTime createdAt, LocalDateTime visitedAt) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        this.createdAt = createdAt;
        this.visitedAt = visitedAt;
    }
}
