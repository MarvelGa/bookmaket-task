package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BookMarket {
    private List<Detail> bid;
    private List<Detail>  ask;

    public BookMarket() {
        this.bid = new ArrayList<>();
        this.ask = new ArrayList<>();
    }
}
