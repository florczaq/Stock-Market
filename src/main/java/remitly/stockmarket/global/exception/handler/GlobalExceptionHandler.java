package remitly.stockmarket.global.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import remitly.stockmarket.global.exception.StockNotFoundException;
import remitly.stockmarket.global.exception.NotEnoughStockException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles exceptions related to insufficient stock or invalid arguments.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity containing the error message and a 400 Bad Request status code.
     */
    @ExceptionHandler({
      NotEnoughStockException.class,
      IllegalArgumentException.class,
      ConstraintViolationException.class,
      HandlerMethodValidationException.class
    })
    public ResponseEntity<String> handleNotEnoughStock (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
    }
    
    /**
     * Handles exceptions related to stock not being found or entity not being found.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity containing the error message and a 404 Not Found status code.
     */
    @ExceptionHandler({StockNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleStockNotFound (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
    }
    
}

