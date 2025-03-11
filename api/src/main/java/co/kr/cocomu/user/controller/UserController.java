package co.kr.cocomu.user.controller;

import co.kr.cocomu.common.api.Api;
import co.kr.cocomu.common.api.NoContent;
import co.kr.cocomu.user.controller.code.UserApiCode;
import co.kr.cocomu.user.controller.docs.UserControllerDocs;
import co.kr.cocomu.user.dto.request.ProfileUpdateDto;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.dto.response.UserResponse;
import co.kr.cocomu.user.service.UserService;
import co.kr.cocomu.user.uploader.ProfileImageUploader;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {

    private final UserService userService;
    private final ProfileImageUploader profileImageUploader;

    @GetMapping("/me")
    public Api<UserResponse> getUser(@AuthenticationPrincipal final Long userId) {
        final UserResponse user = userService.findUser(userId);
        return Api.of(UserApiCode.USER_FOUND_SUCCESS, user);
    }

    @GetMapping("/all") @Deprecated
    public Api<List<UserResponse>> getUsers() {
        final List<UserResponse> users = userService.findAll();
        return Api.of(UserApiCode.ALL_USER_FOUND_SUCCESS, users);
    }

    @PostMapping("/join") @Deprecated
    public Api<UserResponse> createUser(@Valid @RequestBody final UserJoinRequest dto) {
        final UserResponse result = userService.saveUser(dto);
        return Api.of(UserApiCode.USER_JOIN_SUCCESS, result);
    }

    @PostMapping("/me")
    public NoContent updateUser(
        @AuthenticationPrincipal final Long userId,
        @Valid @RequestBody final ProfileUpdateDto dto
    ) {
        userService.updateUser(userId, dto);
        return NoContent.from(UserApiCode.PROFILE_UPDATE_SUCCESS);
    }

    @PostMapping(value = "/profile-image", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Api<String> uploadProfileImage(
        @RequestBody final MultipartFile image,
        @AuthenticationPrincipal final Long userId
    ) {
        final String uploadedUrl = profileImageUploader.uploadProfileImage(image, userId);
        return Api.of(UserApiCode.PROFILE_UPDATE_SUCCESS, uploadedUrl);
    }

}
