package remitly.stockmarket.global.exception;

import jakarta.persistence.EntityNotFoundException;

/**
 * Exception thrown when a requested stock is not found in the bank or in the user's portfolio. This exception indicates
 * that the specified stock does not exist or cannot be located based on the provided criteria.
 */
public class StockNotFoundException extends EntityNotFoundException {
    public StockNotFoundException (String message) {
        super(message);
    }
}
