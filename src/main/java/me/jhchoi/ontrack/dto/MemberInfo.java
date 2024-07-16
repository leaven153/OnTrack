package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class MemberInfo {
    private Long userId;
    private Long projectId;
    private Long memberId;
    private String nickName;
    private String position;
    private LocalDate invitedAt; // position이 invited일 경우, 초대받은 날짜
    private String invitedAs;



//    public Map<Long, String> memberInfo(){
//        Map<Long, String> members = new LinkedHashMap<>();
//        members.put(memberId, nickname);
//        return members;
//    }
}
