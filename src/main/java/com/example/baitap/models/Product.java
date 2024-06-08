package com.example.baitap.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {

    private int id;

    @NotBlank(message = "ten san pham ko duoc de trong")
    private String name;

    @NotNull(message = "gia san pham ko duoc de trong")
    @Min(value = 1, message = "gia san pham ko thap hon 1")
    @Max(value = 999999999, message = "gia san pham ko duoc vuot qua 999999999")
    private long price;

    /*@Length(min = 0, max = 50, message = "hinh anh ko duoc qua 50 ky tu")*/
    private String image;
}
