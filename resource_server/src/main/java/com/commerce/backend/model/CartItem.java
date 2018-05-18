package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart_item")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties("cartItemList") //Prevents infinite recursion
    @JsonIgnore
    private Cart cart;

    @ManyToOne //TODO one to many ??? many to many????
    @JoinColumn(name = "product_id")
    private ProductDisplay cartProduct;

    @Column(name = "amount")
    private Integer amount;

}

