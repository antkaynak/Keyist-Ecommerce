package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("productList")
    private ProductCategory productCategory;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "sell_count")
    @JsonIgnore
    private Integer sellCount;

    @Column(name = "cargo_price")
    private Float cargoPrice;

    @Column(name = "thumb")
    private String thumb;

    @Column(name = "date_created", insertable = false)
    @JsonIgnore
    private String dateCreated;

}