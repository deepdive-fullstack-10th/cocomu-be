package co.kr.cocomu.executor.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.common.BaseExecutorControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.template.PostRequestTemplate;
import co.kr.cocomu.executor.controller.code.ExecutorApiCode;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.service.CodeExecutionProducer;
import co.kr.cocomu.executor.service.StompSseProducer;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(ExecutorController.class)
class ExecutorControllerTest extends BaseExecutorControllerTest {

    private static final String PATH_PREFIX = "/api/v1/executor";

    @MockBean private CodeExecutionProducer codeExecutionProducer;
    @MockBean private CodingSpaceDomainService codingSpaceDomainService;
    @MockBean private StompSseProducer stompSseProducer;

    @Test
    void 코드_실행_요청이_성공한다() {
        // given
        doNothing().when(codingSpaceDomainService).validateActiveTab(1L);
        doNothing().when(codeExecutionProducer).publishCode(any(ExecuteDto.class));
        doNothing().when(stompSseProducer).publishRunning(1L);
        ExecuteDto dto = new ExecuteDto(1L, 1L, "", "", "");

        // when
        String path = PATH_PREFIX + "/execution";
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, dto);

        // then
        NoContent result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(ExecutorApiCode.EXECUTE_CODE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(ExecutorApiCode.EXECUTE_CODE_SUCCESS.getMessage());
    }

}