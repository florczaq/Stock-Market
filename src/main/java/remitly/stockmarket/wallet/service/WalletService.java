package remitly.stockmarket.wallet.service;

import org.springframework.stereotype.Service;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;

@Service
public class WalletService {
    
    public void performStockOperation (String walletId, String stockName, OperationTypeDTO operationType) {
    }
    
    public String getWalletDetails (String walletId) {
        return "Wallet details for " + walletId;
    }
    
    public int getStockQuantity (String walletId, String stockName) {
        return 0;
    }
}
