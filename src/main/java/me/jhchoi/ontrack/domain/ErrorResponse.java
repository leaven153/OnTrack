package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ErrorResponse {
    private String message;
    private Boolean removed; // 해당 할 일이 휴지통으로 옮겨진 경우

}
