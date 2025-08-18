package com.ojo.mullyuojo.domain.user;


import com.ojo.mullyuojo.domain.user.dto.UpdatePasswordDto;
import com.ojo.mullyuojo.domain.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserById(@PathVariable long user_id) {
        return ResponseEntity.ok(userService.getUserById(user_id));
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updatePassword(user.getId(), updatePasswordDto));
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<?> updateUser(@PathVariable long user_id, @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.updateUser(user_id,userRequestDto));

    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable long user_id) {
        return ResponseEntity.ok(userService.deleteUser(user_id));
    }





}
