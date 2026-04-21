package remitly.stockmarket.logs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import remitly.stockmarket.logs.entity.Log;

@Repository
public interface LogsRepository extends JpaRepository<Log, Long> {
}
