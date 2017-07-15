package com.vendingmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vendingmachine.exceptions.NotFullPaidException;
import com.vendingmachine.exceptions.NotSufficientChangeException;
import com.vendingmachine.exceptions.SoldOutException;

public class VendingMachineImpl implements VendingMachine {
	    private Inventory<Coin> cashInventory = new Inventory<Coin>();
	    private Inventory<Item> itemInventory = new Inventory<Item>();  
	    private long totalSales;
	    private Item currentItem;
	    private long currentBalance; 
	   
	    public VendingMachineImpl(){
	        initialize();
	    }
	   
	    private void initialize(){       
	        //initialize machine with 5 coins of each denomination
	        //and 5 cans of each Item       
	        for(Coin c : Coin.values()){
	            cashInventory.put(c, 10);
	        }
	       
	        for(Item i : Item.values()){
	            itemInventory.put(i, 10);
	        }
	       
	    }
	   
	   public long selectItemAndGetPrice(Item item) {
	        if(itemInventory.hasItem(item)){
	            currentItem = item;
	            return currentItem.getPrice();
	        }
	        throw new SoldOutException("Sold Out, Please buy another item");
	    }

	    public void insertCoin(Coin coin) {
	        currentBalance = currentBalance + coin.getDenomination();
	        cashInventory.add(coin);
	    }

	    public Bucket<Item, List<Coin>> collectItemAndChange() {
	        Item item = collectItem();
	        totalSales = totalSales + currentItem.getPrice();
	       
	        List<Coin> change = collectChange();
	       
	        return new Bucket<Item, List<Coin>>(item, change);
	    }
	       
	    private Item collectItem() throws NotSufficientChangeException,
	            NotFullPaidException{
	        if(isFullPaid()){
	            if(hasSufficientChange()){
	                itemInventory.deduct(currentItem);
	                return currentItem;
	            }           
	            throw new NotSufficientChangeException("Not Sufficient change in Inventory");
	           
	        }
	        long remainingBalance = currentItem.getPrice() - currentBalance;
	        throw new NotFullPaidException("Price not full paid, remaining : ", 
	                                          remainingBalance);
	    }
	   
	    private List<Coin> collectChange() {
	        long changeAmount = currentBalance - currentItem.getPrice();
	        List<Coin> change = getChange(changeAmount);
	        updateCashInventory(change);
	        currentBalance = 0;
	        currentItem = null;
	        return change;
	    }
	   
	    public List<Coin> refund(){
	        List<Coin> refund = getChange(currentBalance);
	        updateCashInventory(refund);
	        currentBalance = 0;
	        currentItem = null;
	        return refund;
	    }
	   
	   
	    private boolean isFullPaid() {
	        if(currentBalance >= currentItem.getPrice()){
	            return true;
	        }
	        return false;
	    }

	      
	    private List<Coin> getChange(long amount) throws NotSufficientChangeException{
	        List<Coin> changes = Collections.EMPTY_LIST;
	       
	        if(amount > 0){
	            changes = new ArrayList<Coin>();
	            long balance = amount;
	            while(balance > 0){
	                if(balance >= Coin.twentyfive.getDenomination() 
	                            && cashInventory.hasItem(Coin.twentyfive)){
	                    changes.add(Coin.twentyfive);
	                    balance = balance - Coin.twentyfive.getDenomination();
	                    continue;
	                   
	                }else if(balance >= Coin.ten.getDenomination() 
	                                 && cashInventory.hasItem(Coin.ten)) {
	                    changes.add(Coin.ten);
	                    balance = balance - Coin.ten.getDenomination();
	                    continue;
	                   
	                }else if(balance >= Coin.five.getDenomination() 
	                                 && cashInventory.hasItem(Coin.five)) {
	                    changes.add(Coin.five);
	                    balance = balance - Coin.five.getDenomination();
	                    continue;
	                   
	                }else if(balance >= Coin.one.getDenomination() 
	                                 && cashInventory.hasItem(Coin.one)) {
	                    changes.add(Coin.one);
	                    balance = balance - Coin.one.getDenomination();
	                    continue;
	                   
	                }else{
	                    throw new NotSufficientChangeException("NotSufficientChange,Please try another product");
	                }
	            }
	        }
	       
	        return changes;
	    }
	   
	    public void reset(){
	        cashInventory.clear();
	        itemInventory.clear();
	        totalSales = 0;
	        currentItem = null;
	        currentBalance = 0;
	    } 
	       
	    public void printStats(){
	        System.out.println("Total Sales : " + totalSales);
	        System.out.println("Current Item Inventory : " + itemInventory);
	        System.out.println("Current Cash Inventory : " + cashInventory);
	    }   
	   
	  
	    private boolean hasSufficientChange(){
	        return hasSufficientChangeForAmount(currentBalance - currentItem.getPrice());
	    }
	   
	    private boolean hasSufficientChangeForAmount(long amount){
	        boolean hasChange = true;
	        try{
	            getChange(amount);
	        }catch(NotSufficientChangeException nsce){
	            return hasChange = false;
	        }
	       
	        return hasChange;
	    }

	    private void updateCashInventory(List<Coin> change) {
	        for(Coin c : change){
	            cashInventory.deduct(c);
	        }
	    }
	   
	    public long getTotalSales(){
	        return totalSales;
	    }
	   

}
