package remitly.stockmarket.wallet.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import remitly.stockmarket.wallet.controller.WalletController;

@RestControllerAdvice(assignableTypes = WalletController.class)
public class WalletExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound (EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument (IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
    }
}
