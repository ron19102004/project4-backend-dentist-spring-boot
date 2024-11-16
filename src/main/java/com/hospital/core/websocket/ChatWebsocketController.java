package com.hospital.core.websocket;


import com.hospital.core.entities.account.User;
import com.hospital.infrastructure.utils.ResponseLayout;
import com.hospital.core.mappers.UserWebsocketDetailsMapper;
import com.hospital.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatWebsocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserSocketSessionManager userSocketSessionManager;
    @Autowired
    private UserService userService;

    @MessageMapping("/connect")
    @SendTo("/topic/connect")
    public ResponseLayout<Object> addUser(Long id) {
        User user = User.builder()
                .username("dung")
                .password("123456")
                .build();
        UserWebsocketDetailsMapper userMapper = userSocketSessionManager.addUser(user);
        System.out.println(userSocketSessionManager.getConnectedUsers());
        return ResponseLayout.builder()
                .data(null)
                .message("")
                .success(true)
                .build();
    }
    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnect")
    public ResponseLayout<Object> removeUser(Long id) {
        System.out.println("disconnect:"+id);
        if (userSocketSessionManager.removeUser(id)) {
            return ResponseLayout.builder()
                    .data(null)
                    .message("")
                    .success(true)
                    .build();
        }
        return ResponseLayout.builder()
                .data(null)
                .message("")
                .success(false)
                .build();
    }
}
