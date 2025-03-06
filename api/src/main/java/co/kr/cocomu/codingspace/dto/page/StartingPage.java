package co.kr.cocomu.codingspace.dto.page;

import co.kr.cocomu.codingspace.domain.vo.CodingSpaceStatus;
import co.kr.cocomu.codingspace.dto.response.LanguageDto;
import co.kr.cocomu.codingspace.dto.response.TestCaseDto;
import co.kr.cocomu.codingspace.dto.response.UserDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartingPage {

    private Long id;
    private String name;
    private String description;
    private String workbookUrl;
    private boolean hostMe;
    private int codingMinutes;
    private LocalDateTime startTime;

    private Long tabId;
    private String documentKey;

    private LanguageDto language;
    private List<UserDto> activeUsers;
    private List<TestCaseDto> testCases;

}
