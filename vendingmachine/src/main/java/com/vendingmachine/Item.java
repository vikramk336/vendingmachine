package com.vendingmachine;

public enum Item {
	CHIPS("Chips", 25), BISCUITS("Biscuits", 35), CHOCOLATE("Chocolate", 45);
	   
    private String name;
    private int price;
   
    private Item(String name, int price){
        this.name = name;
        this.price = price;
    }
   
    public String getName(){
        return name;
    }
   
    public long getPrice(){
        return price;
    }

}
