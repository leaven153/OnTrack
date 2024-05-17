package me.jhchoi.ontrack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectController {


    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 생성(team)
     * */

    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 생성(solo)
     * */

    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 초대
     * */


    /**
     * created : 2024-05-
     * param   :
     * return  :
     * explain : 프로젝트 內 할 일 목록 조회
     * */
    @GetMapping("/project")
    public String project(){
        log.info("===============project================");

        /* 확인할 요소:
         * project id: url
         * 프로젝트의 상태: activated
         * 유저의 멤버여부: memberid?,
         * 멤버의 권한: position, capacity
         * */

        return "/project/project";
    }
}
