package org.example;

import org.example.service.AmazonPriceReader;

public class Main {
    public static void main(String[] args) {
        var amazonPriceReader = new AmazonPriceReader();
        amazonPriceReader.readPrices();
    }
}