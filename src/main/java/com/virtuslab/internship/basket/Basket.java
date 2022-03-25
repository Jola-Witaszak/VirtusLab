package com.virtuslab.internship.basket;

import com.virtuslab.internship.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Basket {

    private final List<Product> products;

    public Basket() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Basket)) return false;
        Basket basket = (Basket) o;
        return Objects.equals(getProducts(), basket.getProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProducts());
    }
}
