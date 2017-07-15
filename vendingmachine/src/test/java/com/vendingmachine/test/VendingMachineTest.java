package com.vendingmachine.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.vendingmachine.Bucket;
import com.vendingmachine.Coin;
import com.vendingmachine.Item;
import com.vendingmachine.VendingMachine;
import com.vendingmachine.VendingMachineFactory;
import com.vendingmachine.VendingMachineImpl;
import com.vendingmachine.exceptions.NotSufficientChangeException;
import com.vendingmachine.exceptions.SoldOutException;

public class VendingMachineTest {
    private static VendingMachine vm;
   
    @BeforeClass
    public static void setUp(){
        vm = VendingMachineFactory.createVendingMachine();
    }
   
    @AfterClass
    public static void tearDown(){
        vm = null;
    }
   
    @Test
    public void testBuyItemWithExactPrice() {
        //select item, price in cents
        long price = vm.selectItemAndGetPrice(Item.COKE); 
        //price should be Coke's price      
        assertEquals(Item.COKE.getPrice(), price);
        //25 cents paid              
        vm.insertCoin(Coin.twentyfive);                           
       
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
       
        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change                              
        assertTrue(change.isEmpty());                              
    }
   
    @Test
    public void testBuyItemWithMorePrice(){
        long price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);
       
        vm.insertCoin(Coin.twentyfive);       
        vm.insertCoin(Coin.twentyfive);      
       
        Bucket<Item, List<Coin>> bucket = vm.collectItemAndChange();
        Item item = bucket.getFirst();
        List<Coin> change = bucket.getSecond();
       
        //should be Coke
        assertEquals(Item.SODA, item);
        //there should not be any change                                     
        assertTrue(!change.isEmpty());        
        //comparing change                             
        assertEquals(50 - Item.SODA.getPrice(), getTotal(change));  
       
    }  
  
   
    @Test
    public void testRefund(){
        long price = vm.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);       
        vm.insertCoin(Coin.ten);
        vm.insertCoin(Coin.five);
        vm.insertCoin(Coin.one);
        vm.insertCoin(Coin.twentyfive);
       
        assertEquals(41, getTotal(vm.refund()));       
    }
   
    @Test(expected=SoldOutException.class)
    public void testSoldOut(){
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
        }
     
    }
   
    @Test(expected=NotSufficientChangeException.class)
    public void testNotSufficientChangeException(){
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.SODA);
            vm.insertCoin(Coin.twentyfive);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
           
            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.twentyfive);
            vm.insertCoin(Coin.twentyfive);
            vm.collectItemAndChange();
        }
    }
   
   
    @Test(expected=SoldOutException.class)
    public void testReset(){
        VendingMachine vmachine = VendingMachineFactory.createVendingMachine();
        vmachine.reset();
       
        vmachine.selectItemAndGetPrice(Item.COKE);
       
    }
   
    @Ignore
    public void testVendingMachineImpl(){
        VendingMachineImpl vm = new VendingMachineImpl();
    }
   
    private long getTotal(List<Coin> change){
        long total = 0;
        for(Coin c : change){
            total = total + c.getDenomination();
        }
        return total;
    }
}