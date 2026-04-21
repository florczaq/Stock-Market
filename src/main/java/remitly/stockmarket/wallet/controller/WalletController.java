package remitly.stockmarket.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;
import remitly.stockmarket.wallet.service.WalletService;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    
    @PostMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<Void> performStockOperation (
      @PathVariable String walletId,
      @PathVariable String stockName,
      @RequestBody OperationTypeDTO typeDTO
    ) {
        walletService.performStockOperation(walletId, stockName, typeDTO);
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
