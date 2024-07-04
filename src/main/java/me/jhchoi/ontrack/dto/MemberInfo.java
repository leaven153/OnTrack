package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class MemberInfo {
    private Long userId;
    private Long projectId;
    private Long memberId;
    private String nickName;
    private String position;

//    public Map<Long, String> memberInfo(){
//        Map<Long, String> members = new LinkedHashMap<>();
//        members.put(memberId, nickname);
//        return members;
//    }
}
