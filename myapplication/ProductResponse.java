package com.example.myapplication;

public class ProductResponse {
    public Product product;

    public static class Product {
        public String product_name;
        public String brands;
        public String image_url;
    }
}
