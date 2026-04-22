package remitly.stockmarket.wallet.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;
import remitly.stockmarket.wallet.dto.WalletDTO;
import remitly.stockmarket.wallet.service.WalletService;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
@Validated
public class WalletController {
    private final WalletService walletService;
    
    @PostMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<Void> performStockOperation (
      @PathVariable @NotNull @NotBlank String walletId,
      @PathVariable @NotNull @NotBlank String stockName,
      @RequestBody @Valid OperationTypeDTO typeDTO
    ) {
        walletService.performStockOperation(walletId, stockName, typeDTO);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDTO> getWallet (@PathVariable @NotNull @NotBlank String walletId) {
        return ResponseEntity.ok(walletService.getDetailsWalletById(walletId));
    }
    
    @GetMapping("/{walletId}/stocks/{stockName}")
    public ResponseEntity<Integer> getStockByName (
      @PathVariable @NotNull @NotBlank String walletId,
      @PathVariable @NotNull @NotBlank String stockName
    ) {
        return ResponseEntity.ok(walletService.getStockQuantityByName(walletId, stockName));
    }
    
}
