package com.storerader.server.domain.admin.dto.select.users;

import com.storerader.server.common.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record FindAllUsersListDTO(
        List<FindAllUsersDTO> users,
        Long totalCount,
        int page,
        int size
) {
    public static FindAllUsersListDTO from(Page<UserEntity> pageResult) {
        List<FindAllUsersDTO> users = pageResult
                .getContent()
                .stream()
                .map(FindAllUsersDTO::from)
                .toList();

        return new FindAllUsersListDTO(
                users,
                pageResult.getTotalElements(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }
}