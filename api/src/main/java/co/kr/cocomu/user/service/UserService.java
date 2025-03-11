package co.kr.cocomu.user.service;

import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.dto.request.ProfileUpdateDto;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.dto.response.UserResponse;
import co.kr.cocomu.user.exception.UserExceptionCode;
import co.kr.cocomu.user.repository.UserJpaRepository;
import co.kr.cocomu.user.uploader.ProfileImageUploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final ProfileImageUploader profileImageUploader;

    public UserResponse saveUser(final UserJoinRequest dto) {
        final User user = User.createUser(dto.nickname());
        final User savedUser = userJpaRepository.save(user);
        return new UserResponse(savedUser.getId(), savedUser.getNickname(), savedUser.getProfileImageUrl());
    }

    @Transactional(readOnly = true)
    public User getUserWithThrow(final Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(UserExceptionCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userJpaRepository.findAll()
        .stream()
        .map(User::toDto)
        .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(final Long userId) {
        return getUserWithThrow(userId).toDto();
    }

    public void updateUser(final Long userId, final ProfileUpdateDto dto) {
        final User user = getUserWithThrow(userId);

        if (user.isNotDefaultImage()) {
            profileImageUploader.markAsUnused(user.getProfileImageUrl());
        }
        profileImageUploader.confirmImage(dto.profileImageUrl());

        user.updateProfile(dto.nickname(), dto.profileImageUrl());
    }

}
