package com.commerce.backend.service.cache;

import com.commerce.backend.dao.ProductRepository;
import com.commerce.backend.model.entity.Product;
import com.commerce.backend.model.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@CacheConfig(cacheNames = "product")
public class ProductCacheServiceImpl implements ProductCacheService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductCacheServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Cacheable(key = "#url")
    public Product findByUrl(String url) {
        return productRepository.findByUrl(url).orElse(null);
    }


    @Override
    @Cacheable(key = "#root.methodName", unless = "#result.size()==0")
    public List<Product> findTop8ByOrderByDateCreatedDesc() {
        return productRepository.findTop8ByOrderByDateCreatedDesc();
    }

    @Override
    @Cacheable(key = "{#productCategory.name,#id}", unless = "#result.size()==0")
    public List<Product> getRelatedProducts(ProductCategory productCategory, Long id) {
        List<Product> productList = productRepository.findTop8ByProductCategoryAndIdIsNot(productCategory, id);
        if (productList.size() < 8) {
            productList.addAll(productRepository.findAllByProductCategoryIsNot(productCategory, PageRequest.of(0, 8 - productList.size())));
        }
        return productList;
    }
}
