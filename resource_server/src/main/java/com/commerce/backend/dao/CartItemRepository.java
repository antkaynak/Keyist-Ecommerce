package com.commerce.backend.dao;

import com.commerce.backend.model.CartItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
