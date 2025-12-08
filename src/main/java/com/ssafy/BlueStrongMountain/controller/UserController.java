package com.ssafy.BlueStrongMountain.controller;

import com.ssafy.BlueStrongMountain.dto.BaseResponse;
import com.ssafy.BlueStrongMountain.dto.ChangePasswordRequest;
import com.ssafy.BlueStrongMountain.dto.ChangeUsernameRequest;
import com.ssafy.BlueStrongMountain.dto.UserInfoResponse;
import com.ssafy.BlueStrongMountain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<BaseResponse> changeName(
            @PathVariable Long id,
            @RequestBody ChangeUsernameRequest req
            ) {
        userService.changeUsername(id, req.getUsername());
    //FIXME response type 따로 만들어야 하는지?
        return ResponseEntity.ok(BaseResponse.ok());
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<BaseResponse> changePw(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest req
            ){
        userService.changePassword(id, req.getPassword());
        return ResponseEntity.ok(BaseResponse.ok());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> delete(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(BaseResponse.ok());
    }
}
