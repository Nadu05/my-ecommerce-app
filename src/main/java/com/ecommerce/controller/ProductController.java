package com.ecommerce.controller;

import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        List<Category> categories = categoryService.findAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        
        return "products/list";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
        
        // Get related products (products in the same category)
        List<Product> relatedProducts = productService.findByCategory(product.getCategory());
        // Remove the current product from related products
        relatedProducts.removeIf(p -> p.getId().equals(product.getId()));
        // Limit to 4 related products
        if (relatedProducts.size() > 4) {
            relatedProducts = relatedProducts.subList(0, 4);
        }
        
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        
        return "products/details";
    }

    @GetMapping("/category/{categoryId}")
    public String listProductsByCategory(@PathVariable Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
        
        List<Product> products = productService.findByCategory(category);
        List<Category> categories = categoryService.findAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("currentCategory", category);
        
        return "products/by-category";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam String query, Model model) {
        List<Product> products = productService.searchProducts(query);
        
        model.addAttribute("products", products);
        model.addAttribute("query", query);
        
        return "products/search-results";
    }
}
