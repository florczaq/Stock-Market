package remitly.stockmarket.bank.dto;

import remitly.stockmarket.global.dto.StockDTO;

import java.util.List;

/**
 * Data Transfer Object representing the state of the bank, including a list of stocks.
 *
 * @param stocks A list of StockDTO objects representing the stocks in the bank.
 */
public record BankDTO(List<StockDTO> stocks) {
}
