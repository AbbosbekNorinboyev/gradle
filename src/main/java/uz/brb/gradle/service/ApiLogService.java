package uz.brb.gradle.service;

import org.springframework.data.domain.Pageable;
import uz.brb.gradle.dto.response.Response;

import java.time.LocalDateTime;

public interface ApiLogService {
    Response<?> saveLog(String username, String method, String path, LocalDateTime time, long duration);

    Response<?> getAll(Pageable pageable);
}