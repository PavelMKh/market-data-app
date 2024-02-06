[![Typing SVG](https://readme-typing-svg.demolab.com?font=Fira+Code&size=42&pause=1000&random=false&width=435&lines=Market+data+app)](https://git.io/typing-svg)
## 🚀 About
The application allows you to receive stock exchange data on financial instruments (quotes, candles and other information).
This Spring Boot application is a server for receiving stock data using endpoints.

## ⭐ Features
- Stock prices history from MOEX ISS (https://www.moex.com/a2193)

## 💻 Usage
### Get historic candles from MOEX ISS 
Server endpoint:
```java
GET /stocks/[ticker]/history
```
Request parameters:
* `ticker` - stock ticker
* `candlesize` - candle time period. Possible values: 1 (1 minute), 10 (10 minutes), 60 (1 hour), 24 (1 day), 7 (1 week), 31 (1 month) or 4 (1 quarter)
* `startDate` - start date for receiving candles
* `endDate` - end date for receiving candles

Example of request:
```java
GET /stocks/SBER/history?candlesize=60&startDate=2024-01-22&endDate=2024-02-02
```
## 🛠️ Technology stack
- Java 17 
- Spring Boot - version 3.2.2

## 📖 Disclaimer
The author does not accept responsibility for actions taken on the basis of information obtained as a result of using this program. The author does not represent that the information or opinions provided are correct or complete. The information presented shouldn't be used as the sole guide for making investment decisions. Doesn't individual investment advice.