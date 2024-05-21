package me.jhchoi.ontrack.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String loginPw;

    private String userName;
    private Long userId;
}
