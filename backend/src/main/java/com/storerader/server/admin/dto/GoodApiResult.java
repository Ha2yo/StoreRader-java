package com.storerader.server.admin.dto;

import java.util.List;

record GoodApiResult(
        List<GoodApiItem> item
) {}