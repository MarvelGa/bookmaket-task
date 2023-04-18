package org.example.model;


import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return Objects.equals(price, detail.price) && Objects.equals(size, detail.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, size);
    }
}
