package remitly.stockmarket.wallet.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import remitly.stockmarket.logs.service.LogsService;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class WalletOperationAspect {
    private final LogsService logsService;
    
    @AfterReturning(
      pointcut = "execution(* remitly.stockmarket.wallet.service.WalletService.performStockOperation(..)) && args(walletId, stockName, operationType)",
      argNames = "walletId, stockName, operationType"
    )
    public void logWalletOperation (String walletId, String stockName, Object operationType) {
        log.info("Operation performed: {} on stock {} for wallet {}", operationType, stockName, walletId);
        logsService.saveOperationToLogs(operationType.toString(), walletId, stockName);
    }
}
