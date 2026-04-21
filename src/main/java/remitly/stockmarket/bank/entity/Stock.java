package remitly.stockmarket.bank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import remitly.stockmarket.global.dto.StockDTO;

@Entity
@Table(name = "stock")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("JpaDataSourceORMInspection")
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "stock_name", nullable = false, unique = true)
    private String stockName;
    
    @Column(nullable = false)
    @Min(0)
    public int quantity;
    
    public StockDTO toDTO () {
        return new StockDTO(stockName, quantity);
    }
}