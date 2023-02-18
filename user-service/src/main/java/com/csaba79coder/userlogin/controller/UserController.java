package com.csaba79coder.userlogin.controller;

import com.csaba79coder.api.UsersApi;
import com.csaba79coder.models.UserModel;
import com.csaba79coder.models.UserRegistrationModel;
import com.csaba79coder.userlogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserModel> registerUser(UserRegistrationModel body) {
        return ResponseEntity.status(201).body(userService.saveUser(body));
    }

    @Override
    public ResponseEntity<List<UserModel>> renderAllUsers() {
        return ResponseEntity.status(200).body(userService.renderAllUsers());
    }
}
