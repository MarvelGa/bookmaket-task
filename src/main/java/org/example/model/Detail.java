package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Detail {
    private Integer price;
    private Integer size;

    public Detail(Integer price, Integer size) {
        this.price = price;
        this.size = size;
    }
}
