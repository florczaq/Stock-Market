package remitly.stockmarket.wallet.dto;

public record OperationTypeDTO(String type) {
    public OperationTypeDTO (String type) {
        if (!type.equalsIgnoreCase("BUY") && !type.equalsIgnoreCase("SELL")) {
            throw new IllegalArgumentException("Invalid operation type: " + type);
        }
        this.type = type.toUpperCase();
    }
}
