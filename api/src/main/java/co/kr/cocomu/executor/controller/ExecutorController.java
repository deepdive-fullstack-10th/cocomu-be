package co.kr.cocomu.executor.controller;

import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.executor.controller.code.ExecutorApiCode;
import co.kr.cocomu.executor.controller.docs.ExecutorControllerDocs;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.dto.request.SubmitDto;
import co.kr.cocomu.executor.service.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/executor")
@Slf4j
public class ExecutorController implements ExecutorControllerDocs {

    private final ExecutorService executorService;

    @PostMapping("/execution")
    public NoContent executeCode(@RequestBody final ExecuteDto dto) {
        executorService.execute(dto);
        return NoContent.from(ExecutorApiCode.EXECUTE_CODE_SUCCESS);
    }

    @PostMapping("/submission")
    public NoContent submitCode(@RequestBody final SubmitDto dto) {
        executorService.submit(dto);
        return NoContent.from(ExecutorApiCode.SUBMIT_CODE_SUCCESS);
    }

}
