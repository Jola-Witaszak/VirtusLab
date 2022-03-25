package com.virtuslab.internship.facade;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptGenerator;
import org.springframework.stereotype.Component;

@Component
public class DiscountFacade {

    private final ProductDb productDb;

    public DiscountFacade(ProductDb productDb) {
        this.productDb = productDb;
    }

    public Receipt applyTotalDiscount(Basket basket) {
        var receiptGenerator = new ReceiptGenerator();
        var receipt = receiptGenerator.generate(basket);

        var discount_15 = new FifteenPercentDiscount();
        receipt = discount_15.apply(receipt);

        var discount_10 = new TenPercentDiscount();
        return discount_10.apply(receipt);
    }

    public Product getProduct(String name) {
        return productDb.getProduct(name);
    }
}
