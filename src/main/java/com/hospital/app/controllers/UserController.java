package com.hospital.app.controllers;

import com.hospital.app.entities.User;
import com.hospital.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping("/{id}")
    @PreAuthorize("#user.id==#id")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal User user, @PathVariable("id") Long id){
        return ResponseEntity.ok(userService.findById(id));
    }
}

/**
 * UserControllerVer2 upgrade from UserController as UserControllerVer1
 */
//@RestController
//@RequestMapping("/api/users/v2")
class UserControllerVer2 extends UserController{

}