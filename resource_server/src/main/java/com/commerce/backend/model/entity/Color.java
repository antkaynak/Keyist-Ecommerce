package com.commerce.backend.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "color")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "productVariantList")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "color")
    private List<ProductVariant> productVariantList;

    @Column(name = "name")
    private String name;

    @Column(name = "hex")
    private String hex;

}
