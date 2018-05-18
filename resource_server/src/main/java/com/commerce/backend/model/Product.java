package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties("productList")
    private ProductCategory productCategory;

    @Column(name = "sku")
    @NotBlank
    private String SKU;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "cargo_price")
    private Float cargoPrice;

    @Column(name = "tax_percent")
    private Float taxPercent;

    @Column(name = "cart_desc")
    @NotBlank
    private String cartDesc;

    @Column(name = "long_desc")
    @Type(type = "text")
    @NotBlank
    private String longDesc;

    @Column(name = "thumb")
    @JsonIgnore
    private String thumb;

    @Column(name = "image")
    private String image;

    @Column(name = "date_created", insertable = false)
    @JsonIgnore
    private Date dateCreated;

    @Column(name = "last_updated", insertable = false)
    @Type(type = "timestamp")
    @JsonIgnore
    private Date lastUpdated;

    @Column(name = "stock")
    @NotNull
    private Float stock;

    @Column(name = "sell_count")
    @JsonIgnore
    private Integer sellCount;

    @Column(name = "live")
    @NotNull
    private Integer live;

    @Column(name = "unlimited")
    @NotNull
    private Integer unlimited;

}
