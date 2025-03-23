package com.example.try1.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
    private static final String API_URL = "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public float getUsdRate() {

        RestTemplate restTemplate = new RestTemplate();
        ExchangeRate[] rates = restTemplate.getForObject(API_URL, ExchangeRate[].class);

        if (rates != null) {
            for (ExchangeRate rate : rates) {
                if ("USD".equals(rate.ccy)) {
                    return rate.sale;
                }
            }
        }
        return 0.0f;
    }

    private static class ExchangeRate {
        public String ccy;
        public float sale;
    }
}