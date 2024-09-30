package com.hospital.app.websocket;


import com.hospital.app.entities.User;
import com.hospital.app.helpers.ResponseLayout;
import com.hospital.app.mappers.UserWebsocketDetailsMapper;
import com.hospital.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
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
                .id(id)
                .username("dung")
                .password("123456")
                .build();
        UserWebsocketDetailsMapper userMapper = userSocketSessionManager.addUser(user);
        System.out.println(userSocketSessionManager.getConnectedUsers());
        return new ResponseLayout<>(true,userMapper,"Connected");
    }
    @MessageMapping("/disconnect")
    @SendTo("/topic/disconnect")
    public ResponseLayout<Object> removeUser(Long id) {
        System.out.println("disconnect:"+id);
        if (userSocketSessionManager.removeUser(id)) {
            return new ResponseLayout<Object>(true, null, "Disconnected");
        }
        return new ResponseLayout<Object>(false, null, "User not found");
    }
}
