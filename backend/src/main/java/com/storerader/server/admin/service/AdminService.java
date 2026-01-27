package com.storerader.server.admin.service;

import com.storerader.server.admin.dto.FindAllUsersResponse;
import com.storerader.server.common.entity.UserEntity;
import com.storerader.server.common.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserRepository userRepository;

    @Transactional
    public FindAllUsersResponse findAllUsers() {
        return userRepository.findAll();
    }
}
