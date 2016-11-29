package com.ensimag.server.main;


import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.server.impl.User;
import com.ensimag.services.bank.IBankNode;

public class Main {

	//Neighboors : liste des banNode séparés par des ,
	private void start(String BankName, int BankId, String Neighboors) {
		try {
			Registry registry = LocateRegistry.getRegistry(1099);
			BankNode bank = new BankNode(new Bank(BankId,BankName),BankId);
			String[] neighboorsList =  Neighboors.split(",");
			for(String neighboor : neighboorsList)
			{
				bank.addNeighboor((IBankNode) Naming.lookup("rmi://localhost/"+neighboor));
			}
			registry.rebind(BankName, bank);
			System.out.println("Bank " + BankName + "," + BankId + " initialized");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		if (args[0].equals("rmi"))
		{
			try {
				LocateRegistry.createRegistry(1099);		 
				//System.out.println("Mise en place du Security Manager ...");
				//if (System.getSecurityManager() == null) {
				//System.setSecurityManager(new RMISecurityManager());
				//}
				User user = new User("Premier","Client",20);
				String url = "rmi://" + InetAddress.getLocalHost().getHostAddress() + "/TestRMI";
				System.out.println("Enregistrement de l'objet avec l'url : " + url);
				Naming.rebind(url, (Remote) user);
				System.out.println("Serveur lancé");
				} catch (RemoteException e) {
				e.printStackTrace();
				} catch (MalformedURLException e) {
				e.printStackTrace();
				} catch (UnknownHostException e) {
				e.printStackTrace();
				}
		} else
		{
			Main main = new Main();
			main.start(args[0],Integer.parseInt(args[1]),args[2]);
		}
	}
	

}
