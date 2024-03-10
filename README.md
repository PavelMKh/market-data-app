[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=42&pause=1000&random=false&width=435&lines=Market+data+app)](https://git.io/typing-svg)
## 🚀 About
The application allows you to receive stock exchange data on financial instruments (quotes, candles and other information).
This Spring Boot application is a server for receiving stock data from different sources.  
App supports saving and loading data from a local MongoDB database. To use the feature, you must create a MongoDB database. The default database name is `financialdata_db`, port 27017. You can set your name and port in `application.properties`.

## ⭐ Features
- Stock prices history from MOEX ISS (https://www.moex.com/a2193)
- Historic candles from AlphaVantage (https://www.alphavantage.co/)
- saving data to a local repository (PostgreSQL)
- uploading data for all tickers in your database from the last date to the current one (from MOEX only)
- export candles history in CSV format

## 💻 Usage
### 1. Historic candles from MOEX ISS 
Server endpoint:
```java
GET /moex/shares/[ticker]/history
```
Request parameters:
* `ticker` - stock ticker
* `candlesize` - candle time period. Possible values: 1 (1 minute), 10 (10 minutes), 60 (1 hour), 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)
* `startdate` - start date for receiving candles
* `enddate` - end date for receiving candles

### 2. Historic candles from AlphaVantage
Server endpoint:
```java
GET /global/shares/[ticker]/history
```
Candles can be received for a whole month only
Request parameters:
* `ticker` - stock ticker
* `candlesize` - candle time period. Possible values: 1 (1 minute), 10 (10 minutes), 60 (1 hour), 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)
* `startdate` - first day of the initial month for which candles will be requested
* `enddate` - first day of the last month for which candles will be requested
* `apikey` - API key from AlphaVantage

Example of request:
```java
GET /global/shares/IBM/history?candlesize=60&startdate=2023-03-01&enddate=2023-04-01&apikey=token
```

### 3. Historic candles from Database
```java
GET /repo/shares/[ticker]/history
```
Request parameters:
* `ticker` - stock ticker
* `candlesize` - candle time period. Possible values: 1 (1 minute), 10 (10 minutes), 60 (1 hour), 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)
* `startdate` - first day of the initial month for which candles will be requested
* `enddate` - first day of the last month for which candles will be requested

Example of request:
```java
GET /repo/shares/IBM/history?candlesize=60&startdate=2023-02-21&enddate=2023-03-15
```

### 4. Upload historic candles to Database
```java
GET /repo/reload/moex
```
Request parameters:
* `defaultStartDate` - if there is no date for the specified candle size for ticker in your database, additional loading will begin from this date

Example of request:
```java
GET /repo/reload/moex?defaultStartDate=2024-02-01
```
### 5. Export candles to CSV
#### Request CSV history candles from MOEX ISS:
```java
GET /moex/shares/[ticker]/export-to-csv
```
Request parameters: similar to "1. Historic candles from MOEX ISS"  
#### Request CSV history candles from AlphaVantage:
```java
GET /global/shares/[ticker]/export-to-csv
```
Request parameters: similar to "2. Historic candles from AlphaVantage" 
#### Request CSV history candles from DataBase:  
```java
GET /repo/shares/[ticker]/export-to-csv
```  
Request parameters: similar to "3. Historic candles from Database"
## 🛠️ Technology stack
- Java 17 
- Spring Boot - version 3.2.2
- PostgreSQL 16

## 📖 Disclaimer
The author does not accept responsibility for actions taken on the basis of information obtained as a result of using this program. The author does not represent that the information or opinions provided are correct or complete. The information presented shouldn't be used as the sole guide for making investment decisions. Doesn't individual investment advice.