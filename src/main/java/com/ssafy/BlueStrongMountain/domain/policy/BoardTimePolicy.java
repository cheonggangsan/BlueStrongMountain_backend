package com.ssafy.BlueStrongMountain.domain.policy;

import java.time.LocalDateTime;

public class BoardTimePolicy {
    private BoardTimePolicy(){}
    public static final LocalDateTime NO_DEADLINE
            = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
}
