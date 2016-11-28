package com.ensimag.server.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;

public class BNP {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("BNP", new BankNode(new Bank(2),2));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank initialized");
	}
	public static void main(String[] args) {
		BNP main = new BNP();
		main.start();

	}

}
