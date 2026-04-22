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
    
    /**
     * Endpoint for shutting down the instance after a 1-second delay.
     * Shutting downs runs in separate thread to allow the response to be sent before the instance is shut down.
     *
     * @return A ResponseEntity containing a message indicating that the instance will be shut down.
     */
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
