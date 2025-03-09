package co.kr.cocomu.study.dto.response;

import co.kr.cocomu.study.domain.vo.StudyRole;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "스터디 사용자 응답")
public class StudyMemberDto {

    @Schema(description = "스터디장 식별자", example = "1")
    private Long id;
    @Schema(description = "스터디장 닉네임", example = "코코무")
    private String nickname;
    @Schema(description = "스터디장 프로필 이미지", example = "https://cdn.cocomu.co.kr/images/...")
    private String profileImageUrl;
    @Schema(description = "스터디 권한")
    private StudyRole role;
    @Schema(description = "스터디 가입 일자")
    private LocalDateTime joinedDate;

}
