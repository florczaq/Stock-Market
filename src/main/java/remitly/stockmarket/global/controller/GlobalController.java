package remitly.stockmarket.global.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GlobalController {
    
    private final ApplicationContext context;
    
    @PostMapping("/chaos")
    public ResponseEntity<String> kilInstance () {
        Thread shutdownHook = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            SpringApplication.exit(context, () -> 0);
            log.info("Shutting instance {}-{} down", context.getId(), context.getStartupDate());
        });
        shutdownHook.start();
        return ResponseEntity.ok(
          String.format("Instance %s-%s will be shutdown in 1 second", context.getId(), context.getStartupDate())
        );
    }
}
