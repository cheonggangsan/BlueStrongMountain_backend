package com.ssafy.BlueStrongMountain.dto;

import com.ssafy.BlueStrongMountain.domain.Group;
import com.ssafy.BlueStrongMountain.domain.GroupRole;
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
    private GroupRole groupRole;
    private String createdAt;
    private String updatedAt;

    private GroupSummaryDto(
            final Long id,
            final String title,
            final Long ownerId,
            final String visibility,
            final int memberCount,
            final GroupRole groupRole,
            final String createdAt,
            final String updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.visibility = visibility;
        this.memberCount = memberCount;
        this.groupRole = groupRole;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static GroupSummaryDto from(
            final Group group,
            final GroupRole groupRole,
            final int memberCount
    ) {
        return new GroupSummaryDto(
                group.getId(),
                group.getTitle(),
                group.getOwnerId(),
                group.getVisibility().name(),
                memberCount,
                groupRole,
                group.getCreatedAt().toString(),
                group.getUpdatedAt().toString()
        );
    }
}
