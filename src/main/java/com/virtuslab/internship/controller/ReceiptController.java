package com.virtuslab.internship.controller;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.facade.DiscountFacade;
import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ReceiptController {

	private final DiscountFacade discountFacade;
	
	@Autowired
	public ReceiptController(DiscountFacade discountFacade) {
		this.discountFacade = discountFacade;
	}
	
	@PostMapping(value = "receipt", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Receipt create(@RequestBody Basket basket) {
		return discountFacade.applyTotalDiscount(basket);
	}

	@GetMapping(value = "product/{name}")
	public Product get(@PathVariable String name) {
		try {
			return discountFacade.getProduct(name);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product: " + name + " not exists");
		}
	}
}
