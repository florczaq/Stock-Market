package remitly.stockmarket.bank.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remitly.stockmarket.bank.entity.Stock;
import remitly.stockmarket.bank.exception.StockNotFoundException;
import remitly.stockmarket.bank.repository.StockRepository;
import remitly.stockmarket.global.exception.NotEnoughStockException;

@Service
@RequiredArgsConstructor
public class BankService {
    private final StockRepository stockRepository;
    
    public Stock getStockByName (String name) throws StockNotFoundException {
        return stockRepository
          .findByStockName(name)
          .orElseThrow(
            () -> new StockNotFoundException(
              String.format("Stock with name \"%s\" not found in bank", name))
          );
    }
    
    public void decreaseStockQuantityByOne (String stockName)
      throws EntityNotFoundException, NotEnoughStockException {
        Stock stock = this.getStockByName(stockName);
        
        if (stock.getQuantity() - 1 <= 0) {
            throw new NotEnoughStockException(
              String.format("Not enough stock with name \"%s\" in the bank", stockName));
        }
        
        stockRepository.save(stock.setQuantity(stock.getQuantity() - 1));
    }
    
    
    public void increaseStockQuantityByOne (String stockName) throws EntityNotFoundException {
        Stock stock = this.getStockByName(stockName);
        stockRepository.save(stock.setQuantity(stock.getQuantity() + 1));
    }
}
