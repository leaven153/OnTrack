package me.jhchoi.ontrack.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class LoginUser {

    private String loginId; // email
    private String loginPw;
    private String userName;
    private Long userId;

}
