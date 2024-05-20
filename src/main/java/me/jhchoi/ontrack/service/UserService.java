package me.jhchoi.ontrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.domain.OnTrackTask;
import me.jhchoi.ontrack.domain.OnTrackUser;
import me.jhchoi.ontrack.dto.LoginRequest;
import me.jhchoi.ontrack.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginRequest login(String loginId, String loginPw){
        log.info("service 진입: id={}, pw={}", loginId, loginPw);
        return userRepository.login(loginId, loginPw);
    }

}
