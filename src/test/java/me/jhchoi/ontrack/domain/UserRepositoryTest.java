package me.jhchoi.ontrack.domain;

import me.jhchoi.ontrack.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.util.Date;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void save(){
        OnTrackUser user = OnTrackUser.builder()
                .userEmail("user@abc.com")
                .password("user1234")
                .userName("Jane doe")
                .createdAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .visitedAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        userRepository.save(user);

    }
}
