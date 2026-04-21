package remitly.stockmarket.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remitly.stockmarket.bank.dto.BankDTO;
import remitly.stockmarket.bank.entity.Stock;
import remitly.stockmarket.bank.repository.StockRepository;
import remitly.stockmarket.global.dto.StockDTO;
import remitly.stockmarket.global.exception.NotEnoughStockException;
import remitly.stockmarket.global.exception.StockNotFoundException;

import java.util.List;

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
    
    public List<StockDTO> getAllStocks () {
        return stockRepository.findAll().stream().map(Stock::toDTO).toList();
    }
    
    public void decreaseStockQuantityByOne (String stockName)
      throws StockNotFoundException, NotEnoughStockException {
        Stock stock = this.getStockByName(stockName);
        
        if (stock.getQuantity() - 1 <= 0) {
            throw new NotEnoughStockException(
              String.format("Not enough stock with name \"%s\" in the bank", stockName));
        }
        
        stockRepository.save(stock.setQuantity(stock.getQuantity() - 1));
    }
    
    public void increaseStockQuantityByOne (String stockName) throws StockNotFoundException {
        Stock stock = this.getStockByName(stockName);
        stockRepository.save(stock.setQuantity(stock.getQuantity() + 1));
    }
    
    public void setBankState (BankDTO bankDTO) throws IllegalArgumentException {
        bankDTO.stocks().forEach(stockDTO -> {
            Stock stock = stockRepository
              .findByStockName(stockDTO.name())
              .orElse(new Stock().setStockName(stockDTO.name()));
            if (stockDTO.quantity() < 0) {
                throw new IllegalArgumentException(
                  String.format("Stock quantity cannot be negative for stock with name \"%s\"", stockDTO.name()));
            }
            stock.setQuantity(stockDTO.quantity());
            stockRepository.save(stock);
        });
    }
}
