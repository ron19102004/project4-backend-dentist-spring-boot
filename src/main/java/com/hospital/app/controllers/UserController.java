package com.hospital.app.controllers;

import com.hospital.app.entities.account.User;
import com.hospital.app.services.UserService;
import com.hospital.app.utils.PreAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Default UserController -> UserControllerVer1
 */
@RestController
@RequestMapping("/api/users/v1")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/me")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(user);
    }
    @GetMapping("/test")
    @PreAuthorize(PreAuthUtil.permitALL)
    public ResponseEntity<?> test(){
        return ResponseEntity.ok("Hello World");
    }
}

/**
 * UserControllerVer2 upgrade from UserController as UserControllerVer1
 */
//@RestController
//@RequestMapping("/api/users/v2")
class UserControllerVer2 extends UserController{

}