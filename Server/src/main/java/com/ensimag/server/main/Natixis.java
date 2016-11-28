package com.ensimag.server.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;

public class Natixis {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("Natixis", new BankNode(new Bank(3),3));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank initialized");
	}
	public static void main(String[] args) {
		Natixis main = new Natixis();
		main.start();

	}

}
