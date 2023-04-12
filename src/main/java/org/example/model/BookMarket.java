package org.example.model;


import java.util.LinkedList;
import java.util.List;

public class BookMarket {
    private List<Detail> bid;
    private List<Detail> ask;

    public BookMarket() {
        this.bid = new LinkedList<>();
        this.ask = new LinkedList<>();
    }

    public List<Detail> getBid() {
        return bid;
    }

    public void setBid(List<Detail> bid) {
        this.bid = bid;
    }

    public List<Detail> getAsk() {
        return ask;
    }

    public void setAsk(List<Detail> ask) {
        this.ask = ask;
    }
}
