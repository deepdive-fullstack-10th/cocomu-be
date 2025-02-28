package co.kr.cocomu.user.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import co.kr.cocomu.common.BaseControllerTest;
import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.template.GetRequestTemplate;
import co.kr.cocomu.common.template.PostRequestTemplate;
import co.kr.cocomu.user.controller.code.UserApiCode;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.dto.response.UserResponse;
import co.kr.cocomu.user.service.UserService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@WebMvcTest(UserController.class)
class UserControllerTest extends BaseControllerTest {

    private static final String PATH_PREFIX = "/api/v1/users";

    @MockBean private UserService userService;

    private UserResponse userResponse;

    @BeforeEach
    void setUser() {
        userResponse = new UserResponse(1L, "코코무", "https://cdn.cocomu.co.kr/images/profile.png");
    }

    @Test
    void 사용자_조회가_성공한다() {
        // given
        String path = PATH_PREFIX + "/me";
        given(userService.findUser(1L)).willReturn(userResponse);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path).status(HttpStatus.OK);

        // then
        Api<UserResponse> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserApiCode.USER_FOUND_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserApiCode.USER_FOUND_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(userResponse);
    }

    @Test
    void 모든_사용자_조회가_성공한다() {
        // given
        String path = PATH_PREFIX + "/all";
        List<UserResponse> mockResult = List.of(userResponse);
        given(userService.findAll()).willReturn(mockResult);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path).status(HttpStatus.OK);

        // then
        Api<List<UserResponse>> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserApiCode.ALL_USER_FOUND_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserApiCode.ALL_USER_FOUND_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

    @Test
    void 사용자_추가에_성공한다() {
        // given
        String path = PATH_PREFIX + "/join";
        UserJoinRequest dto = new UserJoinRequest("코코무");
        given(userService.saveUser(dto)).willReturn(userResponse);

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, dto);

        // then
        Api<UserResponse> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserApiCode.USER_JOIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserApiCode.USER_JOIN_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(userResponse);
    }

}