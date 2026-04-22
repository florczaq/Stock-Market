# Stock Market

## Description

This project is my solution to the Remitly Coding Task. It is a Spring Boot application that simulates a simple stock
market with wallet and bank operations.

## Running the project

Prerequisites:

- Docker (with Docker Compose support)
- Run all commands from the repository root

### Windows

Use the PowerShell helper script and pass the host port you want to expose.
Both forms below are valid:

```powershell
.\start.ps1 -Port 8080
.\start.ps1 8080
```

To run another instance, use a different port (for example `8081`):

```powershell
.\start.ps1 -Port 8081
.\start.ps1 8081
```

### Linux/Mac

Make the script executable (first run only), then pass the host port:

```bash
chmod +x ./start.sh
./start.sh 8080
```

### About

The startup scripts:

- build the Docker image for the Spring Boot application,
- start PostgreSQL using Docker Compose,
- start the application container on the host port you provide,
- wait until `actuator/health` reports `UP`, and then print the ready URL.

The database does not use a separate SQL initialization file. The schema is created from Java entities via Spring Data JPA.

## Available Endpoints

| Endpoint                                      | Description                                                                     | Path params                               | Request body       | Response body               | Objects                   |
|-----------------------------------------------|---------------------------------------------------------------------------------|-------------------------------------------|--------------------|-----------------------------|---------------------------|
| `GET /stocks`                                 | Get current bank state (all stocks with prices).                                | none                                      | none               | `BankDTO`                   | `BankDTO`, `StockDTO`     |
| `POST /stocks`                                | Update bank state with a `BankDTO` payload.                                     | none                                      | `BankDTO`          | none (`200 OK`)             | `BankDTO`, `StockDTO`     |
| `POST /wallets/{walletId}/stocks/{stockName}` | Perform wallet stock operation (`BUY` or `SELL`) using `OperationTypeDTO` body. | `walletId` (String), `stockName` (String) | `OperationTypeDTO` | none (`200 OK`)             | `OperationTypeDTO`        |
| `GET /wallets/{walletId}`                     | Get wallet details by wallet ID.                                                | `walletId` (String)                       | none               | `WalletDTO`                 | `WalletDTO`, `StockDTO`   |
| `GET /wallets/{walletId}/stocks/{stockName}`  | Get quantity of a specific stock in a wallet.                                   | `walletId` (String), `stockName` (String) | none               | integer quantity            | none (primitive response) |
| `GET /log`                                    | Get logs of successful stock operations.                                        | none                                      | none               | object with `log: LogDTO[]` | `LogDTO`                  |
| `POST /chaos`                                 | Shutdown the running instance after ~1 second delay.                            | none                                      | none               | String message              | none (String response)    |

### Data transfer objects (DTOs)

#### `BankDTO`

```json
{
  "stocks": [
    {
      "name": "stock_1",
      "quantity": 100
    }
  ]
}
```

- `stocks`: `StockDTO[]` - list of bank stocks.

#### `StockDTO`

```json
{
  "name": "stock_1",
  "quantity": 100
}
```

- `name`: `String` - stock name/symbol used by operations.
- `quantity`: `int` - amount of stock units.

#### `OperationTypeDTO`

```json
{
  "type": "buy"
}
```

- `type`: `String` - allowed values: `buy`, `sell` (case-insensitive input, stored as lowercase).

#### `WalletDTO`

```json
{
  "id": "wallet-123",
  "stocks": [
    {
      "name": "stock_1",
      "quantity": 10
    }
  ]
}
```

- `id`: `String` - wallet identifier.
- `stocks`: `StockDTO[]` - stocks currently held in the wallet.

#### `LogDTO`

```json
{
  "type": "buy",
  "wallet_id": "wallet-123",
  "stock_name": "stock_1"
}
```

- `type`: `String` - operation type (`buy` or `sell`).
- `wallet_id`: `String` - wallet ID where operation happened.
- `stock_name`: `String` - stock name affected by operation.

### Logging

Logging is implemented with an aspect (`WalletOperationAspect`) and persisted via `LogsService`.

- A log entry is created only after a successful wallet stock operation (`POST /wallets/{walletId}/stocks/{stockName}`).
- Stored log data contains: operation type, wallet ID, and stock name.
- Log entries can be read using `GET /log`.
- Failed operations are not saved in logs because logging runs with `@AfterReturning`.

Example response from `GET /log`:

```json
{
  "log": [
    {
      "type": "buy",
      "wallet_id": "wallet-123",
      "stock_name": "stock_1"
    }
  ]
}
```

### Possible problem responses (causes)

The API returns plain text messages for handled errors.

#### `400 Bad Request`

Common causes:
- Invalid operation type in request body (`type` must be `buy` or `sell`).
- Negative stock quantity in `POST /stocks` payload.
- Selling from a wallet that does not contain the stock.
- Selling when wallet stock quantity would go below zero.
- Buying when the bank has no remaining quantity for the selected stock.

Typical endpoints:
- `POST /stocks`
- `POST /wallets/{walletId}/stocks/{stockName}`

#### `404 Not Found`

Common causes:
- Wallet does not exist for wallet read endpoints.
- Stock does not exist in the bank when operation requires it.

Typical endpoints:
- `GET /wallets/{walletId}`
- `GET /wallets/{walletId}/stocks/{stockName}`
- `POST /wallets/{walletId}/stocks/{stockName}`

#### `500 Internal Server Error`

Cause:
- Unexpected, unhandled server-side exception.

Typical response body:

```text
An unexpected error occurred while processing your request.
```

