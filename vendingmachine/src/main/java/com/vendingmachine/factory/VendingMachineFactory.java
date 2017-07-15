package com.vendingmachine.factory;

import com.vendingmachine.VendingMachine;
import com.vendingmachine.VendingMachineImpl;

public class VendingMachineFactory {
	public static VendingMachine createVendingMachine() {
        return new VendingMachineImpl();
    }

}
