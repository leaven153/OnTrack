package me.jhchoi.ontrack.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;



/****************
 * CREATOR: 프로젝트 생성자id
 * type: solo-private, solo-shared, team
 * name: 프로젝트명(20자 이내)
 * url: 프로젝트별 고유 url (초대링크)
 * status: activated - 생성되어 활성화된 상태(생성후 기본값)
 *         finished - 생성자가 완료로 설정한 프로젝트(무료 사용자는 3개까지 가능. 수정 불가능. 읽기만 가능. activated로 복원 가능.)
 *         deactivated - 생성자가 프로젝트를 삭제한지 7일 23:59:58까지의 상태(7일 이내 복구 가능)
 *         discard - 생성자가 프로젝트를 삭제한 지 7일 23:59:59가 지난 프로젝트(복구 불가능)
 *         archived - 유료 서비스를 구독하는 생성자가 종료 후 보관 설정한 프로젝트. (통계 서비스 제공. 기존 내용 수정 불가능. 단, 내용 추가는 가능?)
 * dueDate - 마감일 설정은 null값 가능
 ******************/
@Data // Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor @Builder
public class OnTrackProject {
    private Long id;
    private Long creator; // 프로젝트 생성자
    private String projectType; // 프로젝트 타입: solo-private, solo-shared, team
    private String projectName; // 프로젝트 명(20자 이내)
    private String projectUrl;
    private String projectStatus; // 프로젝트 상태: 활성, 비활성(휴지통), 종료, 삭제예정(discard), 보관됨
    private LocalDate projectDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 프로젝트 정보(기준 성립 요망)가 업데이트 된 일자


}
