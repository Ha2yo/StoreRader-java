package com.storerader.server.domain.admin.dto;

import java.util.List;

public record FindAllGoodsListResponseDTO(
        List<FindAllGoodsDTO> users
) {
    public static FindAllUsersListResponseDTO from(List<FindAllUsersDTO> users) {
        return new FindAllUsersListResponseDTO(users);
    }
}