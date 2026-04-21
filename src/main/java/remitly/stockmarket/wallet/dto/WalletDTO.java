package remitly.stockmarket.wallet.dto;

import remitly.stockmarket.global.dto.StockDTO;

import java.util.List;

public record WalletDTO(String id, List<StockDTO> stocks) {
}
