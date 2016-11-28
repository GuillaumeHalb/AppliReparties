package com.ensimag.server.main;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IBankNode;


public class SocGen {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			BankNode SocGen = new BankNode(new Bank(3),3);
			SocGen.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/Natixis"));
			registry.rebind("SocGen", SocGen);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank SocGen initialized");
	}

	public static void main(String[] args) {
		SocGen main = new SocGen();
		main.start();
	}
	

}
