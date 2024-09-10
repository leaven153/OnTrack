package me.jhchoi.ontrack.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jhchoi.ontrack.service.MemberService;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class NavAlarm {

    private final MemberService memberService;


    public Map<String, Boolean> getAlarm(Long userId){

        // nav에 출력할 알람내용: ① 프로젝트 공지, ②프로젝트 초대, ③휴지통, ④중요 소통
        Map<String, Boolean> navAlarmList = new LinkedHashMap<>();
//        navAlarmList.put("noticeComment", memberService.alarmForNoticeComment(userId));
        navAlarmList.put("projectInvitation", false);
        navAlarmList.put("projectNotice", false);
        navAlarmList.put("bin", memberService.alarmForBin(userId));

        log.info("CALLING navAlarm: {}", navAlarmList);
        // hashMap일 때 - navAlarm 호출: {projectNotice=false, bin=true, noticeComment=false, projectInvitation=false}
        return navAlarmList;
    }
}
