package co.kr.cocomu.user.service;

import co.kr.cocomu.common.exception.domain.NotFoundException;
import co.kr.cocomu.user.domain.User;
import co.kr.cocomu.user.dto.response.UserDto;
import co.kr.cocomu.user.dto.request.UserJoinRequest;
import co.kr.cocomu.user.exception.UserExceptionCode;
import co.kr.cocomu.user.repository.UserJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public UserDto saveUser(final UserJoinRequest dto) {
        final User user = User.createUser(dto.nickname());
        final User savedUser = userJpaRepository.save(user);
        return new UserDto(savedUser.getId(), savedUser.getNickname(), savedUser.getProfileImageUrl());
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userJpaRepository.findAll()
        .stream()
        .map(User::toDto)
        .toList();
    }

    @Transactional(readOnly = true)
    public UserDto findUser(final Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException(UserExceptionCode.USER_NOT_FOUND))
            .toDto();
    }

}
