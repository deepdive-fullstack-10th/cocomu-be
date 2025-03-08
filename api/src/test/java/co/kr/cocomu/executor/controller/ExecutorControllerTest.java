package co.kr.cocomu.executor.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.kr.cocomu.admin.config.AdminUserConfig;
import co.kr.cocomu.codingspace.service.CodingSpaceDomainService;
import co.kr.cocomu.common.BaseExecutorControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.common.template.PostRequestTemplate;
import co.kr.cocomu.executor.controller.code.ExecutorApiCode;
import co.kr.cocomu.executor.dto.message.EventMessage;
import co.kr.cocomu.executor.dto.message.EventType;
import co.kr.cocomu.executor.dto.message.ExecutionMessage;
import co.kr.cocomu.executor.dto.request.ExecuteDto;
import co.kr.cocomu.executor.service.CodeExecutionProducer;
import co.kr.cocomu.executor.service.StompSseProducer;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@WebMvcTest(ExecutorController.class)
@Import(AdminUserConfig.class)
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
        ExecuteDto dto = new ExecuteDto(1L, "", "", "");

        // when
        String path = PATH_PREFIX + "/execution";
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, dto);

        // then
        NoContent result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(ExecutorApiCode.EXECUTE_CODE_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(ExecutorApiCode.EXECUTE_CODE_SUCCESS.getMessage());
    }

    @Test
    void 코드_결과_요청이_성공한다() {
        // given
        ExecutionMessage executionMessage = new ExecutionMessage(1L, "", 0, 0);
        EventMessage<ExecutionMessage> mockMessage = new EventMessage<>(EventType.SUCCESS, executionMessage);
        doNothing().when(stompSseProducer).publishResult(mockMessage);

        String credentials = Base64.getEncoder().encodeToString("cocomu:cocomu1!".getBytes());
        String basicAuth = "Basic " + credentials;

        // when
        String path = PATH_PREFIX + "/result";
        ValidatableMockMvcResponse response = RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", basicAuth)
            .contentType(MediaType.APPLICATION_JSON)
            .body(mockMessage)
            .when().post(path)
            .then().log().all();

        // then
        NoContent result = response.status(HttpStatus.OK).extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(ExecutorApiCode.EXECUTE_SENT_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(ExecutorApiCode.EXECUTE_SENT_SUCCESS.getMessage());
    }

}