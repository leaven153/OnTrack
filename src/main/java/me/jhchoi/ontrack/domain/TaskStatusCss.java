package me.jhchoi.ontrack.domain;


import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public enum TaskStatusCss {
    PAUSE(Arrays.asList("보류", "pause", "pause-bg", "pause-border-shadow")),
    NOT_YET(Arrays.asList("시작 안 함", "not-yet", "notYet-bg20", "notYet-border-shadow")),
    PLANNING(Arrays.asList("계획중", "planning", "planning-bg008", "planning-border-shadow")),
    ING( Arrays.asList("진행중", "ing", "ing-bg008", "ing-border-shadow")),
    REVIEW(Arrays.asList("검토중", "review", "review-bg008", "review-border-shadow")),
    DONE( Arrays.asList("완료", "done", "done-bg008", "done-border-shadow"));

    //    private final Integer status;
    private final List<String> css;

    TaskStatusCss(List<String> css){ // Integer number,
//        this.status = number;
        this.css = css;
    }




}
