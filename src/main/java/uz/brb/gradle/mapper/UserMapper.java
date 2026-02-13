package uz.brb.gradle.mapper;

import org.springframework.stereotype.Component;
import uz.brb.gradle.dto.response.UserResponse;
import uz.brb.gradle.entity.AuthUser;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserResponse toResponse(AuthUser entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .fullName(entity.getFullName())
                .password(entity.getPassword())
                .username(entity.getUsername())
                .role(entity.getRole())
                .build();
    }

    public List<UserResponse> responseList(List<AuthUser> users) {
        if (users != null && !users.isEmpty()) {
            return users.stream().map(this::toResponse).toList();
        }
        return new ArrayList<>();
    }

    public void update(AuthUser entity, UserResponse response) {
        if (response == null) {
            return;
        }
        if (response.getFullName() != null && !response.getFullName().trim().isEmpty()) {
            entity.setFullName(response.getFullName());
        }
        if (response.getPassword() != null && !response.getPassword().trim().isEmpty()) {
            entity.setPassword(response.getPassword());
        }
        if (response.getUsername() != null && !response.getUsername().trim().isEmpty()) {
            entity.setUsername(response.getUsername());
        }
        if (response.getRole() != null) {
            entity.setRole(response.getRole());
        }
    }
}
