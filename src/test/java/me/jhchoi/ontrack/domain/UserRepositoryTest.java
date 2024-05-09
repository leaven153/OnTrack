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
                .userEmail("user2@abc.com")
                .password("user1234")
                .userName("Jane doe")
                .registeredAt(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        userRepository.save(user);

    }
}
