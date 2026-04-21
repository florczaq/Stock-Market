package remitly.stockmarket.wallet.dto;

import java.util.List;

public record OperationTypeDTO(String type) {
    private static final List<String> allowedOperations = List.of("BUY", "SELL");
    
    public OperationTypeDTO (String type) {
        if (type == null || !allowedOperations.contains(type.toUpperCase())) {
            throw new IllegalArgumentException("Invalid operation type: " + type);
        }
        this.type = type.toUpperCase();
    }
}
