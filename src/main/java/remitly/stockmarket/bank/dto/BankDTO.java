package remitly.stockmarket.bank.dto;

import remitly.stockmarket.global.dto.StockDTO;

import java.util.List;

public record BankDTO(List<StockDTO> stocks) {
}
