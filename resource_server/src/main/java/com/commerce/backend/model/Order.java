package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")//cannot name 'order' because it is a reserved word
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
//    @JsonIgnoreProperties("orderList") //Prevents infinite recursion
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetails> orderDetailsList;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount orderDiscount;

    @Column(name = "ship_name")
    @NotBlank
    private String shipName;

    @Column(name = "ship_address")
    @NotBlank
    private String shipAddress;

    @Column(name = "ship_address2")
    private String shipAddress2;

    @Column(name = "city")
    @NotBlank
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    @NotBlank
    private String zip;

    @Column(name = "country")
    @NotBlank
    private String country;

    @Column(name = "phone")
    @NotBlank
    private String phone;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "total_cargo_price")
    private Float totalCargoPrice;

    @Column(name = "email")
    @NotBlank
    private String email;

    @Column(name = "date")
    @Type(type = "timestamp")
    private Date date;

    @Column(name = "shipped")
    private Integer shipped;

    @Column(name = "cargo_firm")
    private String cargoFirm;

    @Column(name = "tracking_number")
    private String trackingNumber;

}