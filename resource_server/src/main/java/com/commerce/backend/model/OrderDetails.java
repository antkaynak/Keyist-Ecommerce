package com.commerce.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "order")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnoreProperties("orderDetailsList")
    @JsonIgnore
    private Order order;

    @ManyToOne //TODO IS IT ONETOONE OR MANY TO ONE
    @JoinColumn(name = "product_id")
    private ProductDisplay productDisplay;

    @Column(name = "amount")
    @NotNull
    private Integer amount;

}
