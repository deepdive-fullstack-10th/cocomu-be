package co.kr.cocomu.codingspace.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "코딩 스페이스 진행 상태")
public enum CodingSpaceStatus {

    WAITING, RUNNING, FEEDBACK, FINISHED

}