package remitly.stockmarket.wallet.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import remitly.stockmarket.bank.entity.Stock;

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
    
    @JoinTable(
      name = "wallet_stocks",
      joinColumns = @JoinColumn(name = "wallet_id"),
      inverseJoinColumns = @JoinColumn(name = "stock_id")
    )
    private List<Wallet_Stocks> stocks;
    
}

