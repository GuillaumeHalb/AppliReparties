package com.ensimag.server.main;


import com.ensimag.server.impl.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Main {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("1000", new BankNode(new Bank(1),1));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank initialized");
	}

	public static void main(String[] args) {
		Main main = new Main();
		main.start();
	}
	

}
