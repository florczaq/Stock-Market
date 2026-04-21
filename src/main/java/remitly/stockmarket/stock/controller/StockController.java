package remitly.stockmarket.stock.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stocks")
public class StockController {
    @GetMapping
    public String getStocks () {
        return "List of stocks";
    }
    
    @PostMapping
    public String addStock () {
        return "Stock added";
    }
}
