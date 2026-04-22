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
    
    /**
     * Get the current state of the bank, including all stocks and their prices.
     *
     * @return A ResponseEntity containing a BankDTO with the current state of the bank.
     */
    @GetMapping
    public ResponseEntity<BankDTO> getStocks () {
        return ResponseEntity.ok(new BankDTO(bankService.getAllStocks()));
    }
    
    /**
     * Update the state of the bank with new stock information.
     *
     * @param bankDTO A BankDTO containing the updated state of the bank.
     * @return A ResponseEntity indicating that the update was successful.
     */
    @PostMapping
    public ResponseEntity<Void> setBankState (@RequestBody BankDTO bankDTO) {
        bankService.setBankState(bankDTO);
        return ResponseEntity.ok().build();
    }
}
