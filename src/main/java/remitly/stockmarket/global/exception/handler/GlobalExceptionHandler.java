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
    
    /**
     * Handles exceptions related to insufficient stock or invalid arguments.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity containing the error message and a 400 Bad Request status code.
     */
    @ExceptionHandler({NotEnoughStockException.class, IllegalArgumentException.class})
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
    
    /**
     * Handles any unexpected exceptions that may occur during request processing.
     *
     * @param e The exception that was thrown.
     * @return A ResponseEntity containing a generic error message and a 500 Internal Server Error status code.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException (Exception e) {
        return new ResponseEntity<>(
          "An unexpected error occurred while processing your request.", HttpStatusCode.valueOf(500));
    }
}

