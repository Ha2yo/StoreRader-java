package com.storerader.server.test;

import com.storerader.server.common.entity.UsersEntity;
import com.storerader.server.common.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestService {
    private final UsersRepository userRepository;

    public List<UsersEntity> findAll() {
        return userRepository.findAll();
    }
}
