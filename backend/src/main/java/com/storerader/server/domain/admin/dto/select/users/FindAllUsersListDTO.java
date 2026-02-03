package com.storerader.server.domain.admin.dto.select.users;

import java.util.List;

public record FindAllUsersListDTO(
        List<FindAllUsersDTO> users
) {
    public static FindAllUsersListDTO from(List<FindAllUsersDTO> users) {
        return new FindAllUsersListDTO(users);
    }
}