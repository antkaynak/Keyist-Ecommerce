package com.commerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "cart")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
//    @JsonIgnoreProperties("user") //Prevents infinite recursion
//    private List<Order> orderList;

    @OneToOne(mappedBy = "cartUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    //    @CustomEmail no email field sent from the client - refer to EmailResetDTO
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;


    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 26)
    @Column(name = "last_name")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 100)
    @Column(name = "city")
    private String city;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    @Column(name = "state")
    private String state;

    @Size(min = 5, max = 6)
    @Column(name = "zip")
    private String zip;

    @Column(name = "email_verified")
    private Integer emailVerified;

    @Column(name = "registration_date", insertable = false)
    @Type(type = "timestamp")
    private Date RegistrationDate;

    @Pattern(regexp = "[0-9]+")
    @Size(min = 8, max = 11)
    @Column(name = "phone")
    private String phone;

    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    @Size(min = 3, max = 40)
    @Column(name = "country")
    private String country;

    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @Size(min = 3, max = 240)
    @Column(name = "address")
    private String address;

    @Pattern(regexp = "[0-9a-zA-Z #,-]+")
    @Size(min = 3, max = 240)
    @Column(name = "address2")
    private String address2;

}