package remitly.stockmarket.global.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import remitly.stockmarket.global.exception.StockNotFoundException;
import remitly.stockmarket.global.exception.NotEnoughStockException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({NotEnoughStockException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleNotEnoughStock (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
    }
    
    @ExceptionHandler({StockNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleStockNotFound (Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
    }
}

