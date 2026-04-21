package remitly.stockmarket.wallet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import remitly.stockmarket.bank.repository.StockRepository;
import remitly.stockmarket.logs.repository.LogsRepository;
import remitly.stockmarket.wallet.repository.WalletRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
class WalletStockFlowIntegrationTest {

    private MockMvc mockMvc;

    @Autowired private WebApplicationContext webApplicationContext;
    @Autowired private WalletRepository walletRepository;
    @Autowired private StockRepository stockRepository;
    @Autowired private LogsRepository logsRepository;

    @BeforeEach
    void setUp () {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        logsRepository.deleteAll();
        walletRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Test
    void fullApiRequirementsFlow () throws Exception {

        // Start with a known bank state.
        mockMvc.perform(post("/stocks")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"stocks": [{"name": "stock1", "quantity": 5}, {"name": "stock2", "quantity": 2}]}
                """))
          .andExpect(status().isOk());

        // Verify the bank exposes the expected stock list.
        mockMvc.perform(get("/stocks"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.stocks.length()").value(2))
          .andExpect(jsonPath("$.stocks[?(@.name == 'stock1')].quantity").value(5))
          .andExpect(jsonPath("$.stocks[?(@.name == 'stock2')].quantity").value(2));

        // Buying a stock that does not exist should return 404.
        mockMvc.perform(post("/wallets/w1/stocks/unknown")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"type": "buy"}
                """))
          .andExpect(status().isNotFound());

        // Buying should fail when the bank has none left.
        mockMvc.perform(post("/stocks")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"stocks": [{"name": "stock1", "quantity": 0}]}
                """))
          .andExpect(status().isOk());

        mockMvc.perform(post("/wallets/w1/stocks/stock1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"type": "buy"}
                """))
          .andExpect(status().isBadRequest());

        // Restore stock quantities for the rest of the flow.
        mockMvc.perform(post("/stocks")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"stocks": [{"name": "stock1", "quantity": 5}, {"name": "stock2", "quantity": 2}]}
                """))
          .andExpect(status().isOk());

        // First successful buy should create the wallet automatically.
        mockMvc.perform(post("/wallets/w1/stocks/stock1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"type": "buy"}
                """))
          .andExpect(status().isOk());

        // Confirm the wallet was created with one unit of stock1.
        mockMvc.perform(get("/wallets/w1"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value("w1"))
          .andExpect(jsonPath("$.stocks[0].name").value("stock1"))
          .andExpect(jsonPath("$.stocks[0].quantity").value(1));

        // Check the per-stock quantity endpoint for the wallet.
        mockMvc.perform(get("/wallets/w1/stocks/stock1"))
          .andExpect(status().isOk())
          .andExpect(content().string("1"));

        // Bank inventory should decrease after a successful buy.
        mockMvc.perform(get("/stocks"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.stocks[?(@.name == 'stock1')].quantity").value(4));

        // Selling from an empty wallet should fail.
        mockMvc.perform(post("/wallets/w2/stocks/stock1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"type": "sell"}
                """))
          .andExpect(status().isBadRequest());

        // Selling from wallet w1 should now succeed.
        mockMvc.perform(post("/wallets/w1/stocks/stock1")
              .contentType(MediaType.APPLICATION_JSON)
              .content("""
                {"type": "sell"}
                """))
          .andExpect(status().isOk());

        // The sold stock should be returned to the bank.
        mockMvc.perform(get("/stocks"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.stocks[?(@.name == 'stock1')].quantity").value(5));

        // Wallet quantity should return to zero.
        mockMvc.perform(get("/wallets/w1/stocks/stock1"))
          .andExpect(status().isOk())
          .andExpect(content().string("0"));

        // Logs should contain only successful operations, in order.
        // We expect: buy, then sell. Failed operations should not be logged.
        mockMvc.perform(get("/log"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.log.length()").value(2))
          .andExpect(jsonPath("$.log[0].type").value("buy"))
          .andExpect(jsonPath("$.log[0].wallet_id").value("w1"))
          .andExpect(jsonPath("$.log[0].stock_name").value("stock1"))
          .andExpect(jsonPath("$.log[1].type").value("sell"))
          .andExpect(jsonPath("$.log[1].wallet_id").value("w1"))
          .andExpect(jsonPath("$.log[1].stock_name").value("stock1"));
    }
}

