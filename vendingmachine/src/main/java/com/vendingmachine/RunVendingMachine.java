package com.vendingmachine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class RunVendingMachine {
    private static VendingMachine vm;

	public static void main(String[] args) {
	    vm= VendingMachineFactory.createVendingMachine();
	    
	    long price = vm.selectItemAndGetPrice(Item.CHIPS); 
	    
	    // BuyItemWithExactPrice
	   // System.out.println(price);
	    vm.insertCoin(Coin.twentyfive);   
	    Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
	    Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
        
        //System.out.println("item:"+item + "change:"+change);
        
        //BuyItemWithMorePrice
        
        price = vm.selectItemAndGetPrice(Item.CHOCOLATE);
        System.out.println(price);
        vm.insertCoin(Coin.twentyfive);       
        vm.insertCoin(Coin.twentyfive);      
       
        bucket = vm.collectItemAndChange();
        item = bucket.getFirst();
        change = bucket.getSecond();
        System.out.println("item:"+item +","+ "change:"+change);
        
        
        //refund
        price = vm.selectItemAndGetPrice(Item.BISCUITS);
        vm.insertCoin(Coin.ten);
        vm.insertCoin(Coin.five);
        vm.insertCoin(Coin.one);
        vm.insertCoin(Coin.twentyfive);
        System.out.println(vm.refund());
    
        //sold out use case
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.CHIPS);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
        }

        //reset

        VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
        vmachine.reset();
       
        System.out.println(vmachine.selectItemAndGetPrice(Item.CHIPS));
       
        //NotSufficientChangeException

        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.CHOCOLATE);
            vm.insertCoin(Coin.twentyfive);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
           
            vm.selectItemAndGetPrice(Item.BISCUITS);
            vm.insertCoin(Coin.twentyfive);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
        }
    
	}

}
