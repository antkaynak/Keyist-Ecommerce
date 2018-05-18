package com.commerce.backend.api;

import com.commerce.backend.model.Product;
import com.commerce.backend.model.ProductCategory;
import com.commerce.backend.model.ProductDisplay;
import com.commerce.backend.service.ProductCategoryService;
import com.commerce.backend.service.ProductDisplayService;
import com.commerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController extends ApiController {


    private final ProductService productService;
    private final ProductDisplayService productDisplayService;
    private final ProductCategoryService productCategoryService;


    @Autowired
    public ProductController(ProductService productService, ProductDisplayService productDisplayService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productDisplayService = productDisplayService;
        this.productCategoryService = productCategoryService;
    }


    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity getAll(@RequestParam("page") Integer page,
                                 @RequestParam("size") Integer size,
                                 @RequestParam(value = "sort", required = false) String sort,
                                 @RequestParam(value = "category", required = false) String category) {

        if (page == null || size == null) {
            throw new IllegalArgumentException("Page and size parameters are required");
        }
        PageRequest pageRequest;
        if (sort != null && !isBlank(sort)) {
            Sort sortRequest = getSort(sort);
            if (sortRequest == null) {
                throw new IllegalArgumentException("Invalid sort parameter");
            }
            pageRequest = PageRequest.of(page, size, sortRequest);
        } else {
            pageRequest = PageRequest.of(page, size);
        }

        if (category != null && !isBlank(category)) {
            ProductCategory productCategory = productCategoryService.findByName(category);
            if (productCategory == null) {
                throw new IllegalArgumentException("Invalid category parameter");
            }
            List returnList = productDisplayService.findAllByProductCategory(pageRequest, productCategory);
            return new ResponseEntity<List>(returnList, HttpStatus.OK);
        }

        List returnList = productDisplayService.findAll(pageRequest);
        return new ResponseEntity<>(returnList, HttpStatus.OK);
    }


    @RequestMapping(value = "/product", method = RequestMethod.GET, params = "id")
    public ResponseEntity getFullById(@RequestParam("id") Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @RequestMapping(value = "/product/related", method = RequestMethod.GET, params = "id")
    public ResponseEntity getByRelated(@RequestParam("id") Long id) {
        ProductDisplay productDisplay = productDisplayService.findById(id);
        if (productDisplay == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List>(productDisplayService.getRelatedProducts(productDisplay.getProductCategory(), id), HttpStatus.OK);
    }

    @RequestMapping(value = "/product/recent", method = RequestMethod.GET)
    public ResponseEntity getByNewlyAdded() {
        List returnList = productDisplayService.findTop8ByOrderByDateCreatedDesc();
        return new ResponseEntity<List>(returnList, HttpStatus.OK);
    }

    @RequestMapping(value = "/product/mostselling", method = RequestMethod.GET)
    public ResponseEntity getByMostSelling() {
        List returnList = productDisplayService.findTop8ByOrderBySellCountDesc();
        return new ResponseEntity<List>(returnList, HttpStatus.OK);
    }


    //TODO rebuild the logic
    @RequestMapping(value = "/product/interested", method = RequestMethod.GET)
    public ResponseEntity getByInterested() {
        List returnList = productDisplayService.findTop8ByOrderBySellCountDesc();
        return new ResponseEntity<List>(returnList, HttpStatus.OK);
    }


    @RequestMapping(value = "/product/search", method = RequestMethod.GET, params = {"page", "size", "keyword"})
    public ResponseEntity searchProduct(@RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @RequestParam("keyword") String keyword) {
        List returnList = productDisplayService.searchProducts(keyword, page, size);
        return new ResponseEntity<List>(returnList, HttpStatus.OK);
    }

    private boolean isBlank(String param) {
        return param.isEmpty() || param.trim().equals("");
    }


    //A better way to do this is storing sorting options in the database
    //and sending those options to the client. Later then the client
    //sends the parameter based upon that.
    private Sort getSort(String sort) {
        switch (sort) {
            case "lowest":
                return new Sort(Sort.Direction.ASC, "price");
            case "highest":
                return new Sort(Sort.Direction.DESC, "price");
            case "name":
                return new Sort(Sort.Direction.ASC, "name");
            default:
                return null;
        }
    }


}
