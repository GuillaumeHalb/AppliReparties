package com.ensimag.server.main;


import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IBankNode;


public class Main {

	private void start(String BankName, int BankId) {
		try {
			Registry registry = LocateRegistry.getRegistry();
			BankNode bank = new BankNode(new Bank(BankId),BankId);
			//bank.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/Natixis"));
			registry.rebind(BankName, bank);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Bank " + BankName + "," + BankId + " initialized");
	}

	public static void main(String[] args) {
		if (args[0].equals("rmi"))
		{
			try {
				Registry registry = LocateRegistry.createRegistry(1099);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else
		{
			Main main = new Main();
			main.start(args[0],Integer.parseInt(args[1]));
		}
	}
	

}
