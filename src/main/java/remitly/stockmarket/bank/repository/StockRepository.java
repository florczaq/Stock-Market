package remitly.stockmarket.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import remitly.stockmarket.bank.entity.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
}
