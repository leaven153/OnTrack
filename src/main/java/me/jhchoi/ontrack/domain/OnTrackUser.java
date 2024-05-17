package me.jhchoi.ontrack.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OnTrackUser {
    private Long id;
    private String userEmail;
    private String userName;
    private String password;
    private LocalDate registeredAt;


    @Builder
    public OnTrackUser(String userEmail, String userName, String password, LocalDate registeredAt) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.password = password;
        this.registeredAt = registeredAt;

    }
}
