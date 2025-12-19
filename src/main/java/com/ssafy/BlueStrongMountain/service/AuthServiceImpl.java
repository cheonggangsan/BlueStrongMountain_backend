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
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private final EmailService emailService;

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

    @Override
    public void logout(LogoutRequest req) {
        String refreshToken = req.getRefreshToken();

        if(!jwtProvider.validateToken(refreshToken)){
            throw new InvalidTokenException();
        }


        Long userId = jwtProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        user = user.withRefreshToken(null);

        userRepository.save(user);
    }

    //TODO 이메일 검증 로직 따로 없음
    @Override
    @Transactional
    public void resetPasswordByEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String newPassword = randomPasswordGenerator(PASS_LENGTH);
            String encoded = passwordEncoder.encode(newPassword);

            User updatedUser = user.withPassword(encoded);

            userRepository.save(updatedUser);

            emailService.sendTemporaryPassword(updatedUser.getEmail(), newPassword);
        });
    }

    private final Integer PASS_LENGTH = 8;
    private final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String randomPasswordGenerator(int length){
        final SecureRandom random = new SecureRandom();
        StringBuilder sb =  new StringBuilder();

        for(int i =0 ; i<length; i++){
            int idx = random.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(idx));
        }
        return sb.toString();
    }
}
