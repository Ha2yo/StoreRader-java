package com.storerader.server.admin.dto;

import java.util.List;

public record FindAllUsersListResponse(
        List<FindAllUsers> users
) {
    public static FindAllUsersListResponse from(List<FindAllUsers> users) {
        return new FindAllUsersListResponse(users);
    }
}