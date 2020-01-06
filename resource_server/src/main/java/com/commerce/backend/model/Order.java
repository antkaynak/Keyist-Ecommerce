package com.commerce.backend.model;

import com.commerce.backend.validator.CustomEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
    @Size(min = 3, max = 52)
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @NotBlank
    private String shipName;

    @Column(name = "ship_address")
    @Size(min = 3, max = 240)
    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @NotBlank
    private String shipAddress;

    @Column(name = "ship_address2")
    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @Size(min = 3, max = 240)
    private String shipAddress2;

    @Column(name = "city")
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @NotBlank
    private String city;

    @Column(name = "state")
    @Size(min = 3, max = 40)
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String state;

    @Column(name = "zip")
    @Size(min = 5, max = 6)
    @Pattern(regexp = "^[0-9]*$")
    @NotBlank
    private String zip;

    @Column(name = "country")
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    @NotBlank
    private String country;

    @Column(name = "phone")
    @Pattern(regexp = "[0-9]+")
    @Size(min = 11, max = 12)
    @NotBlank
    private String phone;

    @Column(name = "total_price")
    private Float totalPrice;

    @Column(name = "total_cargo_price")
    private Float totalCargoPrice;

    @CustomEmail
    @Column(name = "email")
    @Size(min = 3, max = 52)
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