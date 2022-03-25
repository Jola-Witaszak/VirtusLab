package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReceiptGenerator {

    public Receipt generate(Basket basket) {
        List<ReceiptEntry> receiptEntries = new ArrayList<>();

        if (basket != null) {
            Set<Product> products = new HashSet<>(basket.getProducts());
            for (Product product : products) {
                int quantity = 0;
                for (Product sameProduct : basket.getProducts()) {
                    if (product.equals(sameProduct)) {
                        quantity++;
                    }
                }
                receiptEntries.add(new ReceiptEntry(product, quantity));
            }
        }
        return new Receipt(receiptEntries);
    }
}
