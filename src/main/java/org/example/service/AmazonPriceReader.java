package org.example.service;

import org.example.model.AmazonProduct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AmazonPriceReader {

    private static final Map<String, String> COOKIES = Map.of("session-id", "260-1593237-1676605");

    public void readPrices() {
        System.out.println("===AMAZON PRICE READER===");
        System.out.print("Absolute path to file: ");
        var scanner = new Scanner(System.in);
        var userInputFilePath = scanner.nextLine();
        var amazonUrls = getAmazonUrls(userInputFilePath);
        amazonUrls.forEach(this::readPriceOfGivenProduct);
    }

    private void readPriceOfGivenProduct(String url) {
        var document = getMainDocument(url);
        var amazonProduct = new AmazonProduct(document);
        System.out.println(amazonProduct);
    }
    private List<String> getAmazonUrls(String userInputFilePath) {
        if (userInputFilePath == null || userInputFilePath.isEmpty()) {
            throw new IllegalArgumentException("Given path is empty");
        }
        try {
            return Files.readAllLines(new File(userInputFilePath).toPath());
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private Document getMainDocument(String url) {
        try {
            return Jsoup.connect(url)
                    .cookies(COOKIES)
                    .get();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
