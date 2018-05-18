package com.commerce.backend.dao;

import com.commerce.backend.model.Discount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Long> {
    Discount findByCode(String code);
}
