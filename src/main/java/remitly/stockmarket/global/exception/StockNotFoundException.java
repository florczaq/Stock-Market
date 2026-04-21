package remitly.stockmarket.global.exception;

import jakarta.persistence.EntityNotFoundException;

public class StockNotFoundException extends EntityNotFoundException {
    public StockNotFoundException (String message) {
        super(message);
    }
}
