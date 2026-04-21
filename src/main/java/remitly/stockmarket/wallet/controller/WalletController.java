package remitly.stockmarket.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    
    @PostMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<Void> stockOperation (
      @PathVariable String walletId,
      @PathVariable String stockName,
      @RequestBody OperationTypeDTO typeDTO
    ) {
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{walletId}")
    public ResponseEntity<String> getWallet (@PathVariable String walletId) {
        return ResponseEntity.ok("Wallet details for " + walletId);
    }
    
    @GetMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<Integer> getStockByName (@PathVariable String walletId,
      @PathVariable String stockName
    ) {
        return ResponseEntity.ok(0);
    }
    
}
