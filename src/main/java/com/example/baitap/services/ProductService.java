package com.example.baitap.services;

import com.example.baitap.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final List<Product> listProduct = new ArrayList<>();

    public void add(Product newProduct) {
        listProduct.add(newProduct);
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
}
