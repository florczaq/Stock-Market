package remitly.stockmarket.logs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import remitly.stockmarket.logs.dto.LogDTO;
import remitly.stockmarket.logs.service.LogsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogsController {
    private final LogsService logsService;
    
    @GetMapping
    public ResponseEntity<Map<String, List<LogDTO>>> getLogs () {
        return ResponseEntity.ok(Map.of("log", logsService.getLogs()));
    }
}
