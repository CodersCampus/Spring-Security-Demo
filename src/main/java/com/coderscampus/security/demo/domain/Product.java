package com.coderscampus.security.demo.domain;

import java.math.BigDecimal;

public record Product(
        Integer id,
        String name,
        BigDecimal price) {
}

//public class Product {
//
//    private Integer id;
//    private String name;
//    private BigDecimal price;
//    
//    public Product(Integer id, String name, BigDecimal price) {
//        super();
//        this.id = id;
//        this.name = name;
//        this.price = price;
//    }
//    public Integer getId() {
//        return id;
//    }
//    public void setId(Integer id) {
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public BigDecimal getPrice() {
//        return price;
//    }
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }
//}
