package remitly.stockmarket.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    
    /**
     * Retrieves a Stock entity from the database based on its name.
     *
     * @param name The name of the stock to retrieve.
     * @return The Stock entity corresponding to the given name.
     * @throws StockNotFoundException If no stock with the given name is found in the database.
     */
    public Stock getStockByName (String name) throws StockNotFoundException {
        return stockRepository
          .findByStockName(name)
          .orElseThrow(
            () -> new StockNotFoundException(
              String.format("Stock with name \"%s\" not found in bank", name))
          );
    }
    
    /**
     * Retrieves a list of all stocks in the bank, converted to StockDTO objects.
     *
     * @return A list of StockDTO objects representing all stocks in the bank.
     */
    public List<StockDTO> getAllStocks () {
        return stockRepository.findAll().stream().map(Stock::toDTO).toList();
    }
    
    /**
     * Decreases the quantity of a specific stock by one unit.
     *
     * @param stockName The name of the stock to decrease the quantity of.
     * @throws StockNotFoundException  If no stock with the given name is found in the database.
     * @throws NotEnoughStockException If the stock's quantity is already at zero and cannot be decreased further.
     */
    public void decreaseStockQuantityByOne (String stockName)
      throws StockNotFoundException, NotEnoughStockException {
        Stock stock = this.getStockByName(stockName);
        
        if (stock.getQuantity() - 1 < 0) {
            throw new NotEnoughStockException(
              String.format("Not enough stock with name \"%s\" in the bank", stockName));
        }
        
        stockRepository.save(stock.setQuantity(stock.getQuantity() - 1));
    }
    
    /**
     * Increases the quantity of a specific stock by one unit.
     *
     * @param stockName The name of the stock to increase the quantity of.
     * @throws StockNotFoundException If no stock with the given name is found in the database.
     */
    public void increaseStockQuantityByOne (String stockName) throws StockNotFoundException {
        Stock stock = this.getStockByName(stockName);
        stockRepository.save(stock.setQuantity(stock.getQuantity() + 1));
    }
    
    /**
     * Updates the state of the bank with new stock information provided in a BankDTO object.
     *
     * @param bankDTO A BankDTO containing the updated state of the bank, including a list of StockDTO objects.
     * @throws IllegalArgumentException If any stock in the provided BankDTO has a negative quantity.
     */
    @Transactional
    public void setBankState (BankDTO bankDTO) throws IllegalArgumentException {
        if (bankDTO == null) {
            throw new IllegalArgumentException("Bank state payload cannot be null");
        }
        if (bankDTO.stocks() == null) {
            throw new IllegalArgumentException("Bank stocks list cannot be null");
        }
        
        bankDTO.stocks().forEach(stockDTO -> {
            if (stockDTO == null) {
                throw new IllegalArgumentException("Stock entry cannot be null");
            }
            if (stockDTO.quantity() < 0) {
                throw new IllegalArgumentException(
                  String.format("Stock quantity cannot be negative for stock with name \"%s\"", stockDTO.name()));
            }
            Stock stock = stockRepository.findByStockName(stockDTO.name())
              .map(existing -> existing.setQuantity(stockDTO.quantity()))
              .orElseGet(() -> new Stock().setStockName(stockDTO.name()).setQuantity(stockDTO.quantity()));

            stockRepository.save(stock);
        });
    }
}
