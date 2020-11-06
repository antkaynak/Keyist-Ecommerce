package com.commerce.backend.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

    @OneToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;

    @Column(name = "ship_name")
    private String shipName;

    @Column(name = "ship_address")
    private String shipAddress;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "country")
    private String country;

    @Column(name = "phone")
    private String phone;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "total_cargo_price")
    private Float totalCargoPrice;

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