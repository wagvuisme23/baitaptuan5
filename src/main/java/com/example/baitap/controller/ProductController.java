package com.example.baitap.controller;

import com.example.baitap.models.Product;
import com.example.baitap.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${product.images.directory:productImages}")
    private String productImagesDirectory;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        return "products/create";
    }

    @PostMapping("/create")
    public String create(@Valid Product newProduct,
                         @RequestParam MultipartFile imageProduct,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", newProduct);
            return "products/create";
        }
        if (imageProduct != null && !imageProduct.isEmpty()) {
            String imageFilename = saveImage(imageProduct);
            if (imageFilename == null) {
                model.addAttribute("product", newProduct);
                model.addAttribute("errorMessage", "Error saving the image. Please try again.");
                return "products/create";
            }
            newProduct.setImage(imageFilename);
        }
        productService.add(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productService.get(id);
        if (product == null) {
            // Handle product not found, possibly redirect to error page
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product editProduct,
                       @RequestParam MultipartFile imageProduct,
                       BindingResult result,
                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", editProduct);
            return "products/edit";
        }
        if (imageProduct != null && !imageProduct.isEmpty()) {
            String imageFilename = saveImage(imageProduct);
            if (imageFilename == null) {
                model.addAttribute("product", editProduct);
                model.addAttribute("errorMessage", "Error saving the image. Please try again.");
                return "products/edit";
            }
            editProduct.setImage(imageFilename);
        }
        productService.edit(editProduct);
        return "redirect:/products";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        productService.delete(id);
        return "redirect:/products";
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("listproduct", productService.getAll());
        return "products/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Product> searchResults = productService.searchByName(keyword);
        model.addAttribute("listproduct", searchResults);
        return "products/index";
    }

    private String saveImage(MultipartFile imageProduct) {
        try {
            // Ensure the directory exists
            Path uploadDirPath = Paths.get("src/main/resources/static", productImagesDirectory);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            // Get the original filename
            String originalFilename = imageProduct.getOriginalFilename();
            if (originalFilename == null) {
                logger.error("Failed to save image: original filename is null.");
                return null;
            }

            // Generate a unique filename to prevent overwriting existing files
            String imageFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            // Save the file to the target directory
            Path filePath = uploadDirPath.resolve(imageFilename);
            Files.copy(imageProduct.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return imageFilename;
        } catch (IOException e) {
            logger.error("Failed to save image: " + e.getMessage(), e);
            return null;
        }
    }

}
