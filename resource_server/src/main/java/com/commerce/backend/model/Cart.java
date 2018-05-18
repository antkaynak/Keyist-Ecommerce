package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User cartUser;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @NotNull
    private List<CartItem> cartItemList;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount cartDiscount;

    @Column(name = "total_price")
    @NotNull
    private Float totalPrice;

    @Column(name = "total_cargo_price")
    @NotNull
    private Float totalCargoPrice;

    @Column(name = "date_created", insertable = false)
    @JsonIgnore
    private Date dateCreated;


}
