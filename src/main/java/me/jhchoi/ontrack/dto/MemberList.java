package me.jhchoi.ontrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class MemberList {
    private Long userId;
    private Long projectId;
    private Long memberId;
    private String nickName;

//    public Map<Long, String> memberList(){
//        Map<Long, String> members = new LinkedHashMap<>();
//        members.put(memberId, nickname);
//        return members;
//    }
}
