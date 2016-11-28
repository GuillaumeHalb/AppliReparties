package com.ensimag.server.main;


import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;


public class SocGen {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("SG", new BankNode(new Bank(1),1));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank initialized");
	}

	public static void main(String[] args) {
		SocGen main = new SocGen();
		main.start();
	}
	

}
