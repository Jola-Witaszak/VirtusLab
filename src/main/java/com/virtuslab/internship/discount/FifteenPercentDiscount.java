package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;

import java.math.BigDecimal;


public class FifteenPercentDiscount implements Discount {
    public static String NAME = "FifteenPercentDiscount";

    @Override
    public Receipt apply(Receipt receipt) {
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(0.85));
            var discounts = receipt.discounts();
            discounts.add(NAME);
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    }

    private boolean shouldApply(Receipt receipt) {
            int result = receipt.entries().stream()
                    .filter(entry -> entry.product().type().equals(Product.Type.GRAINS))
                    .map(ReceiptEntry::quantity)
                    .reduce(0, Integer::sum);

        return result >= 3;
    }
}
