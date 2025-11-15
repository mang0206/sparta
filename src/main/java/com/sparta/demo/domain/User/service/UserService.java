package com.sparta.demo.domain.User.service;

import com.sparta.demo.common.ServiceException;
import com.sparta.demo.common.ServiceExceptionCode;
import com.sparta.demo.domain.User.dto.UserCreateCommand;
import com.sparta.demo.domain.User.dto.UserDto;
import com.sparta.demo.domain.User.dto.UserUpdateCommand;
import com.sparta.demo.domain.User.entity.User;
import com.sparta.demo.domain.User.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userJpaRepository;

    public UserService(UserRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional
    public void save(User user) {
        userJpaRepository.save(user);
    }

    @Transactional
    public UserDto createUser(UserCreateCommand command) {
        validateDuplicateEmail(command.email(), null);
        User user = User.builder()
                .username(command.username())
                .email(command.email())
                .passwordHash(command.password())
                .build();
        User saved = userJpaRepository.save(user);
        return UserDto.from(saved);
    }

    public List<UserDto> searchUser() {
        return userJpaRepository.findAll()
                .stream()
                .map(UserDto::from)
                .toList();
    }

    public UserDto getUserById(Long userId) {
        return UserDto.from(findUserById(userId));
    }

    @Transactional
    public UserDto updateUser(Long userId, UserUpdateCommand command) {
        User user = findUserById(userId);
        if (!user.getEmail().equals(command.email())) {
            validateDuplicateEmail(command.email(), userId);
        }
        user.updateProfile(command.username(), command.email());
        return UserDto.from(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        userJpaRepository.delete(user);
    }

    private User findUserById(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_USER));
    }

    private void validateDuplicateEmail(String email, Long excludeUserId) {
        boolean exists = excludeUserId == null
                ? userJpaRepository.existsByEmail(email)
                : userJpaRepository.existsByEmailAndIdNot(email, excludeUserId);
        if (exists) {
            throw new ServiceException(ServiceExceptionCode.ALREADY_EXISTS_USER);
        }
    }
}