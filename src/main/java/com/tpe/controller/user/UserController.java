package com.tpe.controller.user;

import com.tpe.payload.request.user.UserRequest;
import com.tpe.payload.response.ResponseMessage;
import com.tpe.payload.response.user.UserResponse;
import com.tpe.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/save/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<UserResponse>> saveUser(
            @RequestBody @Valid UserRequest userRequest, @PathVariable String userRole) {

        return ResponseEntity.ok(userService.saveUser(userRequest,userRole));
    }

}
