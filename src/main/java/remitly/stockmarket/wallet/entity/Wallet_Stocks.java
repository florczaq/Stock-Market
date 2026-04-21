package remitly.stockmarket.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import remitly.stockmarket.bank.entity.Stock;
import remitly.stockmarket.global.dto.StockDTO;

@Entity
@Table(name = "wallet_stocks")
@Getter
@Setter
@Accessors(chain = true)
public class Wallet_Stocks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    
    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;
    
    @Column(nullable = false)
    @Min(0)
    private int quantity;
    
    public StockDTO toDTO () {
        return new StockDTO(stock.getStockName(), quantity);
    }
}
