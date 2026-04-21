package remitly.stockmarket.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import remitly.stockmarket.bank.dto.BankDTO;
import remitly.stockmarket.bank.service.BankService;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;
    
    @GetMapping
    public ResponseEntity<BankDTO> getStocks () {
        return ResponseEntity.ok(new BankDTO(bankService.getAllStocks()));
    }
    
    @PostMapping
    public ResponseEntity<Void> setBankState (@RequestBody BankDTO bankDTO) {
        bankService.setBankState(bankDTO);
        return ResponseEntity.ok().build();
    }
}
