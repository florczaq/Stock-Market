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
    
    public List<LogDTO> getLogs () {
        return logsRepository.findAll().stream()
          .map(Log::toDTO)
          .toList();
    }
    
    public void saveOperationToLogs (String type, String walletId, String stockName) {
        logsRepository.save(new Log()
          .setWalletId(walletId)
          .setStockName(stockName)
          .setOperationType(type));
    }
}
