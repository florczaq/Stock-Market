param(
    [Parameter(Mandatory = $true)]
    [int]$Port
)

$Project = "stockmarket"

docker build -t stockmarket:latest .


$DbName = "stockmarket-db"
$existingDbId = docker ps -aq -f "name=^${DbName}$"

if ($existingDbId) {
    $isRunning = docker inspect -f "{{.State.Running}}" $DbName 2>$null
    if ($isRunning -ne "true") {
        docker start $DbName | Out-Null
    }
    Write-Host "Using existing DB container: $DbName"
} else {
    docker compose -p $Project up -d db
}
docker rm -f "stockmarket-app-$Port" 2>$null
docker run -d `
  --name "stockmarket-app-$Port" `
  --network "${Project}_default" `
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://stockmarket-db:5432/stockmarket `
  -e SPRING_DATASOURCE_USERNAME=sa `
  -e SPRING_DATASOURCE_PASSWORD=secret `
  -e SERVER_PORT=8080 `
  -p "${Port}:8080" `
  stockmarket:latest

Write-Host "App is starting on port $Port..."
do {
    Start-Sleep -Seconds 2
    $status = try { (Invoke-RestMethod "http://localhost:$Port/actuator/health").status } catch { "" }
} until ($status -eq "UP")
Write-Host "App running at http://localhost:$Port"