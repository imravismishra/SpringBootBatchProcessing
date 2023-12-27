package com.ravismishra99.BatchApp.config;

import org.springframework.batch.item.ItemProcessor;
import com.ravismishra99.BatchApp.entity.Product;

public class ProductProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product customer) throws Exception {
//        if(customer.getCountry().equals("United States")) {
//            return customer;
//        }else{
//            return null;
//        }
		return customer;
	}
}