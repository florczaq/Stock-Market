package remitly.stockmarket.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import remitly.stockmarket.wallet.entity.Wallet;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
}
