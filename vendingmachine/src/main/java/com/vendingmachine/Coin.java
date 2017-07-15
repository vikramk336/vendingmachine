package com.vendingmachine;

public enum Coin {
	
	 one(1), five(5), ten(10), twentyfive(25);
	   
    private int denomination;
   
    private Coin(int denomination){
        this.denomination = denomination;
    }
   
    public int getDenomination(){
        return denomination;
    }

}
