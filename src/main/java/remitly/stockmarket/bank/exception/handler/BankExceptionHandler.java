package remitly.stockmarket.bank.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import remitly.stockmarket.bank.controller.BankController;

@RestControllerAdvice(assignableTypes = BankController.class)
public class BankExceptionHandler {
}
