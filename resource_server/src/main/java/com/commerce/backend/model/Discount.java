package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "discount")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"orderList", "cartList"})
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "orderDiscount")
    @JsonIgnore
    private List<Order> orderList;

    @OneToMany(mappedBy = "cartDiscount")
    @JsonIgnore
    private List<Cart> cartList;

    @Column(name = "code")
    @NotBlank
    private String code;

    @Column(name = "discount_percent")
    @NotNull
    private Integer discountPercent;


    @Column(name = "status")
    @JsonIgnore
    private Integer status;

}
