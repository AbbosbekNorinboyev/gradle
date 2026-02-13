package uz.brb.gradle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import uz.brb.gradle.dto.response.Response;
import uz.brb.gradle.dto.response.UserResponse;
import uz.brb.gradle.entity.AuthUser;
import uz.brb.gradle.service.UserService;
import uz.brb.gradle.util.validator.CurrentUser;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/get")
    public Response<?> get(@RequestParam Long id) {
        return userService.get(id);
    }

    @GetMapping("/getAll")
    public Response<?> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return userService.getAll(PageRequest.of(page, size));
    }

    @PutMapping("/update")
    public Response<?> update(@RequestBody UserResponse userResponse) {
        return userService.update(userResponse);
    }

    @GetMapping("/me")
    public Response<?> me(@CurrentUser AuthUser authUser) {
        return userService.me(authUser);
    }

    @GetMapping("/roleStatistics")
    public Response<?> roleStatistics() {
        return userService.roleStatistics();
    }

    @GetMapping("/search")
    public Response<?> search(@RequestParam(required = false) String fullName,
                              @RequestParam(required = false) String username) {
        return userService.search(fullName, username);
    }
}
