package com.ssafy.BlueStrongMountain.service;

import com.ssafy.BlueStrongMountain.auth.JwtProvider;
import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.domain.UserStatus;
import com.ssafy.BlueStrongMountain.dto.*;
import com.ssafy.BlueStrongMountain.exception.*;
import com.ssafy.BlueStrongMountain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public RegisterResponse register(RegisterRequest req) {
        //email, username 중복검증, password 검증
        userRepository.findByEmail(req.getEmail())
                .ifPresent(u -> {throw new EmailAlreadyExistsException();
                });
        userRepository.findByUsername(req.getUsername())
                .ifPresent(u -> {throw new UsernameAlreadyExistsException();
                });
        if(req.getPassword().length() < 8){
            throw new InvalidPasswordException();
        }

        User createdUser = User.createNew(
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                req.getUsername(),
                req.getBaekjoon()
        );

        User savedUser = userRepository.save(createdUser);

        return new RegisterResponse(savedUser.getId(), savedUser.getEmail(),
                savedUser.getUsername(), savedUser.getStatus().name());
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        User foundUser = userRepository.findByEmail(req.getEmail())
                .orElseThrow(AuthenticationFailedException::new);

        boolean match = passwordEncoder.matches(req.getPassword()
                , foundUser.getPassword());
        if(!match) throw new AuthenticationFailedException();

        if(foundUser.getStatus() != UserStatus.ACTIVE){
            throw new UserStatusNotActiveException();
        }

        String accessToken = jwtProvider.generateAccessToken(foundUser);
        String refreshToken = jwtProvider.generateRefreshToken(foundUser);

        User savedUser = foundUser.withRefreshToken(refreshToken);
        userRepository.save(savedUser);

        return new LoginResponse(savedUser.getId(), savedUser.getEmail(),
                savedUser.getUsername(), accessToken, refreshToken);
    }

    @Override
    public UsernameDuplicateResponse checkUsername(String username) {

        boolean isSameName = userRepository.findByUsername(username).isPresent();
        return new UsernameDuplicateResponse(username, isSameName);
    }
}
