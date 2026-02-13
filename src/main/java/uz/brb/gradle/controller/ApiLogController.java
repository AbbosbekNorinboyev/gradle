package uz.brb.gradle.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.brb.gradle.dto.response.Response;
import uz.brb.gradle.service.ApiLogService;

@RestController
@RequestMapping("/api/apiLogs")
@RequiredArgsConstructor
public class ApiLogController {
    private final ApiLogService apiLogService;

    @GetMapping("/getAll")
    public Response<?> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                              @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        return apiLogService.getAll(PageRequest.of(page, size));
    }
}