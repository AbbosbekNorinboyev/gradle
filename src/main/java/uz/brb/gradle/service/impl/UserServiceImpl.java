package uz.brb.gradle.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.brb.gradle.dto.ShortDto;
import uz.brb.gradle.dto.response.Response;
import uz.brb.gradle.dto.response.UserResponse;
import uz.brb.gradle.entity.AuthUser;
import uz.brb.gradle.enums.Role;
import uz.brb.gradle.exception.ResourceNotFoundException;
import uz.brb.gradle.mapper.UserMapper;
import uz.brb.gradle.repository.AuthUserRepository;
import uz.brb.gradle.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static uz.brb.gradle.util.Util.localDateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthUserRepository authUserRepository;
    private final UserMapper userMapper;
    private final EntityManager entityManager;

    @Override
    public Response<?> get(Long id) {
        AuthUser authUser = authUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found: " + id));
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser successfully found")
                .success(true)
                .data(userMapper.toResponse(authUser))
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> getAll(Pageable pageable) {
        List<AuthUser> users = authUserRepository.findAll(pageable).getContent();
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser list successfully found")
                .success(true)
                .data(userMapper.responseList(users))
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> update(UserResponse userResponse) {
        AuthUser authUser = authUserRepository.findById(userResponse.getId())
                .orElseThrow(() -> new ResourceNotFoundException("AuthUser not found: " + userResponse.getId()));
        userMapper.update(authUser, userResponse);
        authUserRepository.save(authUser);
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser successfully updated")
                .success(true)
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> me(AuthUser user) {
        if (user == null) {
            return Response.builder()
                    .code(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .message("USER IS NULL")
                    .success(false)
                    .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                    .build();
        }
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser successfully found")
                .success(true)
                .data(userMapper.toResponse(user))
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> roleStatistics() {
        List<Tuple> roleStatistics = authUserRepository.roleStatistics();
        List<ShortDto> shortDtoList = new ArrayList<>();
        for (Tuple roleStatistic : roleStatistics) {
            shortDtoList.add(
                    new ShortDto(
                            String.valueOf(roleStatistic.get(1, Role.class)),
                            roleStatistic.get(0, Long.class)
                    )
            );
        }
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Role statistics")
                .success(true)
                .data(shortDtoList)
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> search(String fullName, String username) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthUser> query = builder.createQuery(AuthUser.class);
        Root<AuthUser> root = query.from(AuthUser.class);

        List<Predicate> predicates = new ArrayList<>();

        if (fullName != null && !fullName.isEmpty()) {
            predicates.add(builder.like(root.get("fullName"), "%" + fullName + "%"));
        }
        if (username != null && !username.isEmpty()) {
            predicates.add(builder.like(root.get("username"), "%" + username + "%"));
        }

        query.where(predicates.toArray(new Predicate[0]));

        List<AuthUser> authUsers = entityManager.createQuery(query).getResultList();
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("AuthUser list successfully found")
                .success(true)
                .data(userMapper.responseList(authUsers))
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }
}
