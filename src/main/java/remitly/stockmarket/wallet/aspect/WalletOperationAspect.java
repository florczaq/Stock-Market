package remitly.stockmarket.wallet.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import remitly.stockmarket.logs.service.LogsService;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WalletOperationAspect {
    private final LogsService logsService;
    
    /**
     * Aspect method that logs wallet operations after they are performed.
     *
     * @param walletId      The ID of the wallet on which the operation was performed.
     * @param stockName     The name of the stock involved in the operation.
     * @param operationType The type of operation performed (e.g., "BUY", "SELL").
     */
    @AfterReturning(
      pointcut = "execution(* remitly.stockmarket.wallet.service.WalletService.performStockOperation(..)) && args(walletId, stockName, operationType)",
      argNames = "walletId, stockName, operationType"
    )
    public void logWalletOperation (String walletId, String stockName, OperationTypeDTO operationType) {
        log.info("Operation performed: {} on stock {} for wallet {}", operationType, stockName, walletId);
        logsService.saveOperationToLogs(operationType.type(), walletId, stockName);
    }
}
