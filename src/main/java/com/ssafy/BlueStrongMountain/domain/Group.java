package com.ssafy.BlueStrongMountain.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Group {

    private Long id;
    private String title;
    private String description;
    private GroupVisibility visibility;
    private Long ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Group() {
    }

    private Group(
            final String title,
            final String description,
            final GroupVisibility visibility,
            final Long ownerId,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        this.title = title;
        this.description = description;
        this.visibility = visibility;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Group create(
            final String title,
            final String description,
            final GroupVisibility visibility,
            final Long ownerId,
            final LocalDateTime now
    ) {
        return new Group(title, description, visibility, ownerId, now, now);
    }

    public void update(
            final String title,
            final String description,
            final GroupVisibility visibility,
            final LocalDateTime now
    ) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
        if (visibility != null) {
            this.visibility = visibility;
        }
        this.updatedAt = now;
    }

    public void changeOwner(final Long newOwnerId, final LocalDateTime now) {
        this.ownerId = newOwnerId;
        this.updatedAt = now;
    }
    private void setId(Long id){
        this.id = id;
    }
}
