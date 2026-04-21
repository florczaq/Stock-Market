package remitly.stockmarket.global.exception;

public class NotEnoughStockException extends IllegalArgumentException {
    public NotEnoughStockException (String message) {
        super(message);
    }
}
