package org.example.model;


import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BookMarket {
    private List<Detail> bid;
    private List<Detail> ask;

    private List<Detail> spread;

    public BookMarket() {
        this.bid = new LinkedList<>();
        this.ask = new LinkedList<>();
        this.spread = new LinkedList<>();
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

    public List<Detail> getSpread() {
        return spread;
    }

    public void setSpread(List<Detail> spread) {
        this.spread = spread;
    }

    public void setAsk(List<Detail> ask) {
        this.ask = ask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookMarket that = (BookMarket) o;
        return Objects.equals(bid, that.bid) && Objects.equals(ask, that.ask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bid, ask);
    }
}
