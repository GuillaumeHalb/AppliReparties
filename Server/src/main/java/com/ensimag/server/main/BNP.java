package com.ensimag.server.main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IBankNode;

public class BNP {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			BankNode BNP = new BankNode(new Bank(2),2);
			BNP.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/SocGen"));
			registry.rebind("BNP", BNP);
			registry.rebind("BNP", new BankNode(new Bank(2),2));
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank BNP initialized");
	}
	public static void main(String[] args) {
		BNP main = new BNP();
		main.start();

	}

}
