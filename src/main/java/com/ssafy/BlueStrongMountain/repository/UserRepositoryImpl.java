package com.ssafy.BlueStrongMountain.repository;

import com.ssafy.BlueStrongMountain.domain.User;
import com.ssafy.BlueStrongMountain.repository.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository{
    private final UserMapper mapper;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            mapper.insert(user);  // id 자동 생성됨
            return user;
        }
        mapper.update(user);
        return user;
        //throw new UnsupportedOperationException("User update는 별도 구현 필요");
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(mapper.findByEmail(email));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(mapper.findByUsername(username));
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public void delete(Long id) {
        mapper.delete(id);
    }
}
