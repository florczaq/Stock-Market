package remitly.stockmarket.global.exception;

public class StockNotFoundInWalletException extends RuntimeException {
    public StockNotFoundInWalletException (String message) {
        super(message);
    }
}
