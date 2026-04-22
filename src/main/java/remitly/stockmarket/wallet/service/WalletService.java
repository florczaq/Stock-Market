package remitly.stockmarket.wallet.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import remitly.stockmarket.bank.entity.Stock;
import remitly.stockmarket.global.exception.StockNotFoundException;
import remitly.stockmarket.bank.service.BankService;
import remitly.stockmarket.wallet.dto.WalletDTO;
import remitly.stockmarket.wallet.entity.Wallet_Stocks;
import remitly.stockmarket.global.exception.NotEnoughStockException;
import remitly.stockmarket.wallet.exception.WalletNotFoundException;
import remitly.stockmarket.wallet.repository.WalletRepository;
import remitly.stockmarket.wallet.dto.OperationTypeDTO;
import remitly.stockmarket.wallet.entity.Wallet;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final BankService bankService;
    
    @Transactional
    public void performStockOperation (
      @NotNull @NotBlank String walletId,
      @NotNull @NotBlank String stockName,
      OperationTypeDTO operationType
    ) throws StockNotFoundException, NotEnoughStockException {
        if (!walletRepository.existsById(walletId)) {
            walletRepository.save(new Wallet().setId(walletId));
        }
        
        switch (operationType.type()) {
            case "buy" -> {
                bankService.decreaseStockQuantityByOne(stockName);
                this.increaseStockQuantityByOne(walletId, stockName);
            }
            case "sell" -> {
                this.decreaseStockQuantityByOne(walletId, stockName);
                bankService.increaseStockQuantityByOne(stockName);
            }
            default -> throw new IllegalArgumentException("Invalid operation type: " + operationType.type());
        }
    }
    
    @Transactional
    protected void decreaseStockQuantityByOne (String walletName, String stockName)
      throws EntityNotFoundException, NotEnoughStockException {
        Wallet wallet = walletRepository
          .findById(walletName)
          .orElseThrow(() -> new WalletNotFoundException("Wallet with id " + walletName + " not found"));

        Wallet_Stocks wallet_stocks = getWalletStocks(wallet).stream()
          .filter(ws -> ws.getStock().getStockName().equals(stockName))
          .findFirst()
          .orElseThrow(() -> new NotEnoughStockException(
            "Stock with name " + stockName + " not found in wallet " + walletName));
        
        if (wallet_stocks.getQuantity() - 1 < 0) {
            throw new NotEnoughStockException(
              "Not enough stock with name " + stockName + " in wallet " + walletName);
        }
        
        if (wallet_stocks.getQuantity() - 1 == 0) {
            getWalletStocks(wallet).remove(wallet_stocks);
        } else {
            wallet_stocks.setQuantity(wallet_stocks.getQuantity() - 1);
        }
        walletRepository.save(wallet);
    }
    
    @Transactional
    protected void increaseStockQuantityByOne (String walletName, String stockName) throws StockNotFoundException {
        Wallet wallet = walletRepository
          .findById(walletName)
          .orElseThrow(() -> new EntityNotFoundException("Wallet with id " + walletName + " not found"));

        List<Wallet_Stocks> walletStocks = getWalletStocks(wallet);

        Wallet_Stocks wallet_stocks = walletStocks.stream()
          .filter(ws -> ws.getStock().getStockName().equals(stockName))
          .findFirst()
          .orElseGet(() -> {
              Stock stock = bankService.getStockByName(stockName);
              Wallet_Stocks created = new Wallet_Stocks()
                .setWallet(wallet)
                .setStock(stock)
                .setQuantity(0);
              walletStocks.add(created);
              return created;
          });

        wallet_stocks.setQuantity(wallet_stocks.getQuantity() + 1);
        walletRepository.save(wallet);
    }
    
    public WalletDTO getDetailsWalletById (String walletId) throws WalletNotFoundException {
        return walletRepository
          .findById(walletId)
          .map(Wallet::toDTO)
          .orElseThrow(() -> new WalletNotFoundException("Wallet with id " + walletId + " not found"));
    }
    
    public int getStockQuantityByName (String walletId, String stockName) throws WalletNotFoundException {
        Wallet wallet = walletRepository
          .findById(walletId)
          .orElseThrow(() -> new WalletNotFoundException("Wallet with id " + walletId + " not found"));
        
        return getWalletStocks(wallet).stream()
          .filter(ws -> ws.getStock().getStockName().equals(stockName))
          .findFirst()
          .map(Wallet_Stocks::getQuantity)
          .orElse(0);
    }

    private List<Wallet_Stocks> getWalletStocks (Wallet wallet) {
        if (wallet.getStocks() == null) {
            wallet.setStocks(new ArrayList<>());
        }
        return wallet.getStocks();
    }
}
