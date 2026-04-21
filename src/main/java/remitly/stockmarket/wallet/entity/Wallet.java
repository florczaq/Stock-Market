package remitly.stockmarket.wallet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "wallet")
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

