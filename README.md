# Spring Boot Excel Data Parser

This is a **Spring Boot**-based application that automates the process of parsing product data from **Rozetka**, fetching the current USD exchange rate via **PrivatBank's API**, converting prices, and saving the results into an **Excel file**. It also stores product data in a local **H2 database** for further use.

## Key Features:

- **Fetch USD Exchange Rate**: The app connects to **PrivatBank's REST API** to get the latest USD exchange rate.
- **Parse Products from Rozetka**: Collects product data such as name, price in UAH and USD, image URL, and product link.
- **Currency Conversion**: All prices are automatically converted to USD using the latest exchange rate.
- **Excel Export**: The application generates an Excel file with all collected product data.
- **Data Storage**: All parsed products are also stored in a local **H2 database**.

## How It Works:

1. The user runs the application.
2. The app connects to PrivatBank's API and fetches the current exchange rate.
3. Then it parses product data from Rozetka: names, prices in UAH, images, and links.
4. Prices are converted to USD using the retrieved exchange rate.
5. All data is saved into both an **Excel file** and the **H2 database**.

## How to Run:

1. Download the **`BAT`** and **`JAR`** files from the **Releases** section on GitHub.
2. Run the **`BAT`** file on your computer.
3. Once started, access the app via **`localhost:8080`** to interact with the interface.
