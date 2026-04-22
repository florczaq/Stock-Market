package remitly.stockmarket.global.exception;

/**
 * Exception thrown when there is an attempt to buy or sell more stocks that are available in
 * the bank or in the user's portfolio. This exception indicates that the requested quantity
 * of stocks cannot be fulfilled due to insufficient stock availability.
 */
public class NotEnoughStockException extends IllegalArgumentException {
    public NotEnoughStockException (String message) {
        super(message);
    }
}
