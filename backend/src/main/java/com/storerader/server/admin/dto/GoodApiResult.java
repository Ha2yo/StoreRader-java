package com.storerader.server.admin.dto;

import java.util.List;

public record GoodApiResult(
        List<GoodApiItem> item
) {}