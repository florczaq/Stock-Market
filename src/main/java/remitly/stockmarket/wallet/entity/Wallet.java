package remitly.stockmarket.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import remitly.stockmarket.global.dto.StockDTO;
import remitly.stockmarket.wallet.dto.WalletDTO;

import java.util.List;

@Entity
@Table(name = "wallet")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("JpaDataSourceORMInspection")
public class Wallet {
    @Id
    private String id;
    
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wallet_Stocks> stocks;
    
    public WalletDTO toDTO () {
        List<StockDTO> stockList = stocks
          .stream()
          .map(Wallet_Stocks::toDTO)
          .toList();
        
        return new WalletDTO(id, stockList);
    }
}

