package com.example.try1.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import com.example.try1.model.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductParserService {

    private static final String URL = "https://rozetka.com.ua/notebooks/c80004/";

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL).get();

            // Шукаємо блоки товарів
            Elements items = doc.select(".goods-tile");

            for (Element item : items) {
                String title = item.select(".goods-tile__title").text();


                String priceStr = item.select(".goods-tile__price-value").text();
                float price = parsePrice(priceStr);

                String imageUrl = item.select(".goods-tile__picture img").attr("src");
                String productUrl = item.select(".goods-tile__heading a").attr("href");

                products.add(new Product(title, price, imageUrl, productUrl));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    private float parsePrice(String priceStr) {
        try {
            String cleanedPrice = priceStr.replaceAll("[^0-9.,]", "").replace(",", ".");
            return Float.parseFloat(cleanedPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0f;
        }
    }
}