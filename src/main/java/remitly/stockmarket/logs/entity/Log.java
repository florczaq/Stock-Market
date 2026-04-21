package remitly.stockmarket.logs.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import remitly.stockmarket.logs.dto.LogDTO;

import java.util.Locale;

@Entity
@Table(name = "logs")
@NoArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
@Setter
@Getter
@Accessors(chain = true)
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "type", nullable = false)
    @Pattern(regexp = "buy|sell", message = "Operation type must be either BUY or SELL")
    String operationType;
    
    @Column(name = "stock_name", nullable = false)
    String stockName;
    
    @Column(name = "wallet_id", nullable = false)
    String walletId;
    
    public Log setOperationType (String operationType) {
        this.operationType = operationType.toLowerCase(Locale.ROOT);
        return this;
    }
    
    public Log (long id, String operationType, String stockName, String walletId) {
        this.id = id;
        this.stockName = stockName;
        this.walletId = walletId;
        this.setOperationType(operationType);
    }
    
    public LogDTO toDTO () {
        return new LogDTO(this.operationType, this.walletId, this.stockName);
    }
}
