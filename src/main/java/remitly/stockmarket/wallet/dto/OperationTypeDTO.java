package remitly.stockmarket.wallet.dto;

import java.util.List;
import java.util.Locale;

public record OperationTypeDTO(String type) {
    private static final List<String> allowedOperations = List.of("buy", "sell");
    
    public OperationTypeDTO (String type) {
        if (type == null || !allowedOperations.contains(type.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Invalid operation type: " + type);
        }
        this.type = type.toLowerCase(Locale.ROOT);
    }
}
