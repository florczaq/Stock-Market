package remitly.stockmarket.bank.exception;

import jakarta.persistence.EntityNotFoundException;

public class StockNotFoundException extends EntityNotFoundException {
    public StockNotFoundException (String message) {
        super(message);
    }
}
