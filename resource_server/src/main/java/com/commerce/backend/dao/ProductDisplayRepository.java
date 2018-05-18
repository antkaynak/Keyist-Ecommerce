package com.commerce.backend.dao;


import com.commerce.backend.model.ProductCategory;
import com.commerce.backend.model.ProductDisplay;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDisplayRepository extends PagingAndSortingRepository<ProductDisplay, Long> {
    List<ProductDisplay> findAllByProductCategory(Pageable pageable, ProductCategory productCategory);

    List<ProductDisplay> findTop8ByOrderByDateCreatedDesc();

    List<ProductDisplay> findTop8ByOrderBySellCountDesc();

    List<ProductDisplay> findTop8ByProductCategoryAndIdIsNotOrderBySellCountDesc(ProductCategory productCategory, Long id);

    List<ProductDisplay> findAllByProductCategoryIsNotOrderBySellCountDesc(ProductCategory productCategory, Pageable pageable);

    List<ProductDisplay> findAllByNameContaining(String name, Pageable pageable);
}
