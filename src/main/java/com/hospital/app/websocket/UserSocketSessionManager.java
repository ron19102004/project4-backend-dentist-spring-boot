package com.hospital.app.websocket;

import com.hospital.app.entities.User;
import com.hospital.app.mappers.UserWebsocketDetailsMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
public class UserSocketSessionManager {
    private final Set<UserWebsocketDetailsMapper> connectedUsers = ConcurrentHashMap.newKeySet();
    private boolean isExist(UserWebsocketDetailsMapper userMapper){
        return connectedUsers.stream()
                .anyMatch(u -> u.equals(userMapper));
    }
    public UserWebsocketDetailsMapper addUser(User user) {
        UserWebsocketDetailsMapper userMapper = UserWebsocketDetailsMapper.from(user);
        if(isExist(userMapper)){
            return userMapper;
        }
        connectedUsers.add(userMapper);
        return userMapper;
    }

    public boolean removeUser(Long id) {
        UserWebsocketDetailsMapper user = connectedUsers
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(user != null) {
            connectedUsers.remove(user);
            return true;
        }
        return false;
    }
}
