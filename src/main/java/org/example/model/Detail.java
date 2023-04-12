package org.example.model;


public class Detail {
    private Integer price;
    private Integer size;

    public Detail(Integer price, Integer size) {
        this.price = price;
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
