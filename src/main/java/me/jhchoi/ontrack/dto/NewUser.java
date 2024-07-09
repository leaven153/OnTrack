package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class NewUser {
    private String userEmail;
    private String password;
    private String userName;
    private String verificationCode;
    private Boolean verified;
}
