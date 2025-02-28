package co.kr.cocomu.user.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.jwt.JwtProvider;
import co.kr.cocomu.common.security.SecurityConfig;
import co.kr.cocomu.template.GetRequestTemplate;
import co.kr.cocomu.template.PostRequestTemplate;
import co.kr.cocomu.user.controller.code.UserStatusCode;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.dto.response.UserDto;
import co.kr.cocomu.user.service.UserService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UserController.class)
@WithMockUser
@Import(SecurityConfig.class)
class UserControllerTest {

    private static final String PATH_PREFIX = "/api/v1/users";

    @MockBean private UserService userService;
    @MockBean private JwtProvider jwtProvider;

    private UserDto userDto;

    @BeforeEach
    void setUser(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        userDto = new UserDto(1L, "코코무", "https://cdn.cocomu.co.kr/images/profile.png");
        doNothing().when(jwtProvider).validationTokenWithThrow(anyString());
        given(jwtProvider.getUserId(anyString())).willReturn(1L);
    }

    @Test
    void 사용자_조회가_성공한다() {
        // given
        String path = PATH_PREFIX + "/me";
        given(userService.findUser(1L)).willReturn(userDto);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path).status(HttpStatus.OK);

        // then
        Api<UserDto> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserStatusCode.USER_FOUND_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserStatusCode.USER_FOUND_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(userDto);
    }

    @Test
    void 모든_사용자_조회가_성공한다() {
        // given
        String path = PATH_PREFIX + "/all";
        List<UserDto> mockResult = List.of(userDto);
        given(userService.findAll()).willReturn(mockResult);

        // when
        ValidatableMockMvcResponse response = GetRequestTemplate.execute(path).status(HttpStatus.OK);

        // then
        Api<List<UserDto>> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserStatusCode.ALL_USER_FOUND_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserStatusCode.ALL_USER_FOUND_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(mockResult);
    }

    @Test
    void 사용자_추가에_성공한다() {
        // given
        String path = PATH_PREFIX + "/join";
        UserJoinRequest dto = new UserJoinRequest("코코무");
        given(userService.saveUser(dto)).willReturn(userDto);

        // when
        ValidatableMockMvcResponse response = PostRequestTemplate.executeWithBody(path, dto);

        // then
        Api<UserDto> result = response.extract().as(new TypeRef<>() {});
        assertThat(result.code()).isEqualTo(UserStatusCode.USER_JOIN_SUCCESS.getCode());
        assertThat(result.message()).isEqualTo(UserStatusCode.USER_JOIN_SUCCESS.getMessage());
        assertThat(result.result()).isEqualTo(userDto);
    }

}