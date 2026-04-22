package remitly.stockmarket.bank.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import remitly.stockmarket.bank.controller.BankController;

/**
 * Exception handler for the BankController. This class will handle exceptions thrown by any method in the BankController
 * and provide appropriate responses to the client.
 */
@RestControllerAdvice(assignableTypes = BankController.class)
public class BankExceptionHandler {
}
