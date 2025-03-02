package co.kr.cocomu.study.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudyDetailPageDto {

    private String name;
    private List<LanguageDto> languages;
    private String codingSpaces;

}
