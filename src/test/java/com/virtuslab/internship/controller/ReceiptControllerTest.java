package com.virtuslab.internship.controller;

import com.google.gson.Gson;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.facade.DiscountFacade;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountFacade discountFacade;

    @Test
    void shouldCreateReceiptWithAllDiscounts() throws Exception {
        //Given
        var productDb = new ProductDb();
        var basket = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var cereals1 = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        var steak = productDb.getProduct("Steak");
        basket.addProduct(cereals);
        basket.addProduct(cereals1);
        basket.addProduct(bread);
        basket.addProduct(steak);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(basket);

        var entries = List.of(new ReceiptEntry(cereals, 2, BigDecimal.valueOf(16)),
                new ReceiptEntry(bread, 1, BigDecimal.valueOf(5)),
                new ReceiptEntry(steak, 1, BigDecimal.valueOf(50)));
        var discounts = List.of("FifteenPercentDiscount", "TenPercentDiscount");
        var totalPrice = bread.price().add(cereals.price()).add(cereals.price()).add(steak.price());
        var receipt = new Receipt(entries, discounts, totalPrice);

        when(discountFacade.applyTotalDiscount(basket)).thenReturn(receipt);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/receipt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));
    }

    @Test
    void shouldCreateReceiptWithNoDiscountsWhenTotalPriceIsBelow50AndLessThan3GrainProducts() throws Exception {
        //Given
        var productDb = new ProductDb();
        var basket = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        var orange = productDb.getProduct("Orange");
        basket.addProduct(cereals);
        basket.addProduct(bread);
        basket.addProduct(orange);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(basket);

        var entries = List.of(new ReceiptEntry(cereals, 1, BigDecimal.valueOf(8)),
                new ReceiptEntry(bread, 1, BigDecimal.valueOf(5)),
                new ReceiptEntry(orange, 1, BigDecimal.valueOf(5)));
        List<String> discounts = new ArrayList<>();
        var totalPrice = bread.price().add(cereals.price()).add(orange.price());
        var receipt = new Receipt(entries, discounts, totalPrice);

        when(discountFacade.applyTotalDiscount(basket)).thenReturn(receipt);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/receipt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));
    }

    @Test
    void shouldCreateReceiptWithOneDiscountWhenThereAre3GrainProductsAndLessThan50TotalPrice() throws Exception {
        //Given
        var productDb = new ProductDb();
        var basket = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var cereals1 = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        basket.addProduct(cereals);
        basket.addProduct(cereals1);
        basket.addProduct(bread);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(basket);

        var entries = List.of(new ReceiptEntry(cereals, 2, BigDecimal.valueOf(16)),
                new ReceiptEntry(bread, 1, BigDecimal.valueOf(5)));
        var discounts = List.of("FifteenPercentDiscount");
        var totalPrice = bread.price().add(cereals.price()).add(cereals.price());
        var receipt = new Receipt(entries, discounts, totalPrice);

        when(discountFacade.applyTotalDiscount(basket)).thenReturn(receipt);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/receipt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));
    }

    @Test
    void shouldCreateReceiptWithOneDiscountWhenTotalPriceIsAtLeast50AndLessThan3GrainProducts() throws Exception {
        //Given
        var productDb = new ProductDb();
        var basket = new Basket();
        var cereals = productDb.getProduct("Cereals");
        var bread = productDb.getProduct("Bread");
        var steak = productDb.getProduct("Steak");
        basket.addProduct(cereals);
        basket.addProduct(bread);
        basket.addProduct(steak);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(basket);

        var entries = List.of(new ReceiptEntry(cereals, 1, BigDecimal.valueOf(8)),
                new ReceiptEntry(bread, 1, BigDecimal.valueOf(5)),
                new ReceiptEntry(steak, 1, BigDecimal.valueOf(50)));
        var discounts = List.of("TenPercentDiscount");
        var totalPrice = bread.price().add(cereals.price()).add(steak.price());
        var receipt = new Receipt(entries, discounts, totalPrice);

        when(discountFacade.applyTotalDiscount(basket)).thenReturn(receipt);

        //When & Then
        mockMvc.perform(MockMvcRequestBuilders
                .post("/receipt")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.entries", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.discounts", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(totalPrice));
    }
}