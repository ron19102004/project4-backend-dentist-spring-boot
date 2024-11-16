package com.hospital.core.mappers;

import com.hospital.core.entities.account.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Mapper set User to UserWebsocketDetailsMapper
 */
@Builder
@ToString
@Getter
public class UserWebsocketDetailsMapper {
    private Long id;
    private String username;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserWebsocketDetailsMapper that = (UserWebsocketDetailsMapper) obj;
        return this.id.equals(that.id);
    }

    public static UserWebsocketDetailsMapper from(User user) {
        return UserWebsocketDetailsMapper.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
