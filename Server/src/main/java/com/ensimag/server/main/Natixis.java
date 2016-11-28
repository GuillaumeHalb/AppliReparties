package com.ensimag.server.main;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankNode;
import com.sun.javafx.collections.MappingChange.Map;
import com.ensimag.server.impl.Account;

public class Natixis {

	private void start() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			BankNode Natixis = new BankNode(new Bank(1),1);
			Natixis.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/BNP"));
			registry.rebind("Natixis", Natixis);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank Natixis initialized");
	}
	public static void main(String[] args) {
		Natixis main = new Natixis();
		main.start();

	}

}
