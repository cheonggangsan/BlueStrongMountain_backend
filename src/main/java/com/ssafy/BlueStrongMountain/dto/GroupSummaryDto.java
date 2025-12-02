package com.ssafy.BlueStrongMountain.dto;

import com.ssafy.BlueStrongMountain.domain.Group;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GroupSummaryDto {

    private Long id;
    private String title;
    private Long ownerId;
    private String visibility;
    private int memberCount;
    private String createdAt;
    private String updatedAt;

    private GroupSummaryDto(
            final Long id,
            final String title,
            final Long ownerId,
            final String visibility,
            final int memberCount,
            final String createdAt,
            final String updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.visibility = visibility;
        this.memberCount = memberCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroupSummaryDto from(
            final Group group,
            final int memberCount
    ) {
        return new GroupSummaryDto(
                group.getId(),
                group.getTitle(),
                group.getOwnerId(),
                group.getVisibility().name(),
                memberCount,
                group.getCreatedAt().toString(),
                group.getUpdatedAt().toString()
        );
    }
}
