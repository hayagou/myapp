package com.hayagou.myapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends Time {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;

    private float price;

    private String desc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product updateProduct(String name, float price){
        this.name = name;
        this.price = price;
        return this;
    }
}
