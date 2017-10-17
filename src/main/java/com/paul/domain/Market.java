package com.paul.domain;

public enum Market {
    EU("EU", "Europe"),
    UK("UK", "United Kingdom"),
    MX("MX", "Mexico"),
    US("US", "United States");

    private String market;
    private String marketFullName;

    Market(String market, String marketFullName){
        this.market = market;
        this.marketFullName = marketFullName;
    }

    public String getMarket() {
        return market;
    }

    public String getMarketFullName(){
        return marketFullName;
    }
}
