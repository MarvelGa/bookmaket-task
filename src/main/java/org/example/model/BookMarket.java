package org.example.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class BookMarket {
    private List<Detail> bid;
    private List<Detail> ask;

    public BookMarket() {
        this.bid = new LinkedList<>();
        this.ask = new LinkedList<>();
    }
}
