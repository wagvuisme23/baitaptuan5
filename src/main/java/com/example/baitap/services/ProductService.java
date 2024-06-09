package com.example.baitap.services;

import com.example.baitap.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final List<Product> listProduct = new ArrayList<>();
    private int counter = 1;  // Initialize the counter

    public void add(Product newProduct) {
        newProduct.setId(counter);  // Set the id for the new product
        listProduct.add(newProduct);
        counter++;  // Increment the counter
    }

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void edit(Product editProduct) {
        Product find = get(editProduct.getId());
        if (find != null) {
            find.setName(editProduct.getName());
            find.setImage(editProduct.getImage());
            find.setPrice(editProduct.getPrice());
        }
    }


    public void delete(int id) {
        listProduct.removeIf(p -> p.getId() == id);
    }

    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return listProduct;
        }
        return listProduct.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
