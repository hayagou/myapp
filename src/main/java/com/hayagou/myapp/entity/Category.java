package com.hayagou.myapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Category extends Time{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long categoryId;

    private String name;

    public Category(String name) {
        this.name = name;
    }
}
