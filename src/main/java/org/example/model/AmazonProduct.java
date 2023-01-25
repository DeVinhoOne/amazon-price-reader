package org.example.model;

import lombok.Getter;
import org.jsoup.nodes.Document;

@Getter
public class AmazonProduct {
    private String brand;
    private Price price;

    public AmazonProduct(Document document) {
        setBrand(document);
        setPrice(document);
    }

    private void setBrand(Document document) {
        var brandElements = document.getElementsByClass("a-spacing-small po-brand");
        if (brandElements.isEmpty()) {
            System.err.println("Unable to establish BRAND NAME for given product");
            brand = "";
        } else {
            brand = brandElements.get(0)
                    .getElementsByTag("td")
                    .get(1)
                    .getElementsByTag("span")
                    .text();
        }
    }

    private void setPrice(Document document) {
        var elements = document.select("#corePrice_feature_div > div > span > span.a-offscreen");
        if (elements.isEmpty()) {
            System.err.println("Unable to establish PRICE for given product");
            price = new Price(0d, Currency.UNKNOWN);
        }
        var dirtyPrice = elements.get(0).childNodes().get(0).toString();
        price = parsePrice(dirtyPrice);
    }

    private Price parsePrice(String dirtyPrice) {
        if (dirtyPrice == null || dirtyPrice.isEmpty())
            throw new IllegalArgumentException("Given price is empty or null.");
        var a = dirtyPrice.replaceAll("&nbsp;", "")
                .replaceAll("zł", "")
                .replaceAll("€", "")
                .replaceAll(",", ".");
        return new Price(Double.parseDouble(a), getCurrency(dirtyPrice));
    }

    private Currency getCurrency(String dirtyPrice) {
        Currency[] currencies = Currency.values();
        for (Currency currency : currencies) {
            if (dirtyPrice.contains(currency.value)) return currency;
        }
        return Currency.UNKNOWN;
    }

    @Override
    public String toString() {
        return "brand='" + brand + '\'' +
                ", price=" + price.value() +
                ", currency=" + price.currency();
    }
}
