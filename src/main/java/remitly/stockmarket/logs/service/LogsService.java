package remitly.stockmarket.logs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remitly.stockmarket.logs.dto.LogDTO;
import remitly.stockmarket.logs.entity.Log;
import remitly.stockmarket.logs.repository.LogsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogsService {
    private final LogsRepository logsRepository;
    
    /**
     * Retrieves a list of all logs from the database, converted to LogDTO objects.
     *
     * @return A list of LogDTO objects representing all logs in the database.
     */
    public List<LogDTO> getLogs () {
        return logsRepository.findAll().stream()
          .map(Log::toDTO)
          .toList();
    }
    
    /**
     * Saves a new log entry to the database with the specified operation type, wallet ID, and stock name.
     *
     * @param type      The type of operation (e.g., "BUY", "SELL") to be logged.
     * @param walletId  The ID of the wallet associated with the operation.
     * @param stockName The name of the stock involved in the operation.
     */
    public void saveOperationToLogs (String type, String walletId, String stockName) {
        logsRepository.save(new Log()
          .setWalletId(walletId)
          .setStockName(stockName)
          .setOperationType(type));
    }
}
