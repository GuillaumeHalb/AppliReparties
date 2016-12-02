package com.ensimag.server.main;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.server.impl.User;
import com.ensimag.services.bank.IBankNode;

public class Main {

	//Neighboors : liste des banNode séparés par des ,
	private void start(String BankName, int BankId) {
		try {
			Registry registry = LocateRegistry.getRegistry(1099);
			BankNode bank = new BankNode(new Bank(BankId,BankName),BankId);
			registry.rebind(BankName, bank);
			System.out.println("Bank " + BankName + "," + BankId + " initialized");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void addNeighboors(String BankName,String Neighboors) throws RemoteException, NotBoundException, MalformedURLException
	{
		System.out.println("on rentre...");
		Registry registry = LocateRegistry.getRegistry(1099);
		IBankNode bank = (IBankNode) Naming.lookup("rmi://localhost/" + BankName);
		System.out.println("id : " + bank.getId());
 			String[] neighboorsList =  Neighboors.split(",");
			for(String neighboor : neighboorsList)
			{
				bank.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/" + neighboor));
			}
		
	}
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		if (args[0].equals("rmi"))
		{
			try {
					Registry registry = LocateRegistry.createRegistry(1099);
					User user = new User("Lebit","Benji",23);
					String client = "client";
					System.out.println("Enregistrement de l'objet avec le nom : " + client);
					registry.rebind(client, user);
					System.out.println("Serveur lancé");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		} else
		{
			Main main = new Main();
			if (args[2].equals("start"))
				main.start(args[0],Integer.parseInt(args[1]));
			else if (args[2].equals("add"))
				main.addNeighboors(args[0], args[1]);
		}
	}
	

}
