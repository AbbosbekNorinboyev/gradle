package uz.brb.gradle.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.brb.gradle.dto.response.Response;
import uz.brb.gradle.entity.ApiLog;
import uz.brb.gradle.repository.ApiLogRepository;
import uz.brb.gradle.service.ApiLogService;
import uz.brb.gradle.service.logic.RedisCacheService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static uz.brb.gradle.util.Util.localDateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ApiLogServiceImpl implements ApiLogService {
    private final ApiLogRepository apiLogRepository;
    private final RedisCacheService redisCacheService;
    private final ExecutorService executorService;
    private static final String CACHE_KEY = "gradle";

    @Override
    public Response<?> saveLog(String username, String method, String path, LocalDateTime time, long duration) {
        executorService.execute(() -> {
            ApiLog apiLog = ApiLog.builder()
                    .username(username)
                    .method(method)
                    .path(path)
                    .timestamp(time)
                    .durationMs(duration)
                    .build();
            apiLogRepository.save(apiLog);
        });
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message("ApiLog successfully saved")
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }

    @Override
    public Response<?> getAll(Pageable pageable) {
        List<ApiLog> apiLogs = apiLogRepository.findAll(pageable).getContent();
        redisCacheService.saveData(CACHE_KEY, apiLogs, 10, TimeUnit.MINUTES);
        Object data = redisCacheService.getData(CACHE_KEY);
        return Response.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .success(true)
                .message("ApiLog list successfully found")
                .data(data)
                .timestamp(localDateTimeFormatter(LocalDateTime.now()))
                .build();
    }
}
