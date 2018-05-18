package com.commerce.backend.service;

import com.commerce.backend.dao.ProductDisplayRepository;
import com.commerce.backend.model.ProductCategory;
import com.commerce.backend.model.ProductDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "product_display")
public class ProductDisplayServiceImpl implements ProductDisplayService {


    private final ProductDisplayRepository productDisplayRepository;

    @Autowired
    public ProductDisplayServiceImpl(ProductDisplayRepository productDisplayRepository) {
        this.productDisplayRepository = productDisplayRepository;
    }

    @Override
    @Cacheable(key = "#id")
    public ProductDisplay findById(Long id) {
        Optional optional = productDisplayRepository.findById(id);
        return optional.isPresent() ? (ProductDisplay) optional.get() : null;
    }

    @Override
    @Cacheable(key = "#pageable")
//    @CacheEvict(key="#pageable", allEntries = true)
    public List<ProductDisplay> findAll(Pageable pageable) {
        return productDisplayRepository.findAll(pageable).getContent();
    }

    @Override
    @Cacheable(key = "{#pageable,#productCategory.name}")
    public List<ProductDisplay> findAllByProductCategory(Pageable pageable, ProductCategory productCategory) {
        return productDisplayRepository.findAllByProductCategory(pageable, productCategory);
    }

    @Override
    @Cacheable(key = "#root.methodName")
    public List<ProductDisplay> findTop8ByOrderByDateCreatedDesc() {
        return productDisplayRepository.findTop8ByOrderByDateCreatedDesc();
    }

    @Override
    @Cacheable(key = "#root.methodName")
    public List<ProductDisplay> findTop8ByOrderBySellCountDesc() {
        return productDisplayRepository.findTop8ByOrderBySellCountDesc();
    }

    @Override
    @CachePut(key = "'findTop8ByOrderBySellCountDesc'")
    public List<ProductDisplay> findTop8ByOrderBySellCountDescCacheRefresh() {
        return productDisplayRepository.findTop8ByOrderBySellCountDesc();
    }

    @Override
    @Cacheable(key = "{#productCategory.name,#id}")
    public List<ProductDisplay> getRelatedProducts(ProductCategory productCategory, Long id) {
        List<ProductDisplay> returnList = productDisplayRepository.findTop8ByProductCategoryAndIdIsNotOrderBySellCountDesc(productCategory, id);
        if (returnList.size() < 8) {
            returnList.addAll(productDisplayRepository.findAllByProductCategoryIsNotOrderBySellCountDesc(productCategory, PageRequest.of(0, 8 - returnList.size())));
        }
        return returnList;
    }

    @Override
    public List<ProductDisplay> searchProducts(String keyword, Integer page, Integer size) {
        if (page == null || size == null) {
            throw new IllegalArgumentException("Page and size parameters are required");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return productDisplayRepository.findAllByNameContaining(keyword, pageRequest);
    }


}
