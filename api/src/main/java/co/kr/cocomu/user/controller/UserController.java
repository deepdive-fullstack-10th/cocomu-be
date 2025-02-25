package co.kr.cocomu.user.controller;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.exception.domain.BadRequestException;
import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.user.controller.docs.UserControllerDocs;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.dto.UserDto;
import co.kr.cocomu.user.controller.code.UserStatusCode;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.dto.response.AllUserResponse;
import co.kr.cocomu.user.exception.UserExceptionCode;
import co.kr.cocomu.user.repository.UserJpaRepository;
import co.kr.cocomu.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;

    @GetMapping("/me/{userId}")
    public Api<UserDto> getUser(@PathVariable final Long userId) {
        final UserDto user = userService.findUser(userId);
        return Api.of(UserStatusCode.USER_FOUND_SUCCESS, user);
    }

    @GetMapping("/all")
    public Api<List<UserDto>> getUsers() {
        final List<UserDto> users = userService.findAll();
        final AllUserResponse result = new AllUserResponse(users);

        return Api.of(UserStatusCode.ALL_USER_FOUND_SUCCESS, users);
    }

    @PostMapping("/join")
    public Api<UserDto> createUser(@Valid @RequestBody final UserJoinRequest dto) {
        final UserDto result = userService.saveUser(dto);
        return Api.of(UserStatusCode.USER_JOIN_SUCCESS, result);
    }

}
