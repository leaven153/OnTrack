package me.jhchoi.ontrack.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class OnTrackUser {
    private Long id;
    private String userEmail;
    private String userName;
    private String password;
    private LocalDate registeredAt;
    private Boolean verified;
    private String verificationCode;
}
