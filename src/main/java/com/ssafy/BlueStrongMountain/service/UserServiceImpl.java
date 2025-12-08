package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.dto.UserInfoResponse;
import com.ssafy.BlueStrongMountain.exception.InvalidPasswordException;
import com.ssafy.BlueStrongMountain.exception.UserHasGroupException;
import com.ssafy.BlueStrongMountain.exception.UserNotFoundException;
import com.ssafy.BlueStrongMountain.exception.UsernameAlreadyExistsException;
import com.ssafy.BlueStrongMountain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final GroupService groupService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfoResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return new UserInfoResponse(
                user.getId(), user.getEmail(), user.getUsername(),
                user.getBaekjoonHandle(),
                user.getStatus().name(), user.getCreatedAt(), user.getUpdatedAt()
        );
    }

    @Override
    public void changeUsername(Long userId, String newName) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        userRepository.findByUsername(newName)
                .ifPresent(u -> {throw new UsernameAlreadyExistsException();});

        User updatedUser = user.withUsername(newName);
        userRepository.save(updatedUser);
    }

    @Override
    public void changePassword(Long userId, String newPw) {
        if(newPw.length() < 8) throw new InvalidPasswordException();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        User updatedUser = user.withPassword(passwordEncoder.encode(newPw));
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        //group에 속해 있으면 예외처리
        if(!groupService.findMyGroups(userId).isEmpty()){
            throw new UserHasGroupException();
        }

        User updatedUser = user.inactive();
        userRepository.save(updatedUser);
    }
}
