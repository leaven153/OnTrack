package me.jhchoi.ontrack.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.jhchoi.ontrack.domain.ProjectMember;
import me.jhchoi.ontrack.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


}
