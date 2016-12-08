package com.ensimag.client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.server.impl.Account;
import com.ensimag.server.impl.AddAccount;
import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.server.impl.Message;
import com.ensimag.server.impl.User;
import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IAction;
import com.ensimag.services.message.IMessage;
import com.ensimag.services.message.IResult;

public class Main {
	
	private static long messageId = 0;
	
	public static void afficherListeBanques(ArrayList<IBankNode> bankNodeList) throws RemoteException {
		for (IBankNode bank : bankNodeList) {
			System.out.println("Nom : " + bank.getBankName());
		}
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
		ArrayList<IBankNode> bankNodeList = new ArrayList<IBankNode>();
		IBankNode GoldmanSachs = (IBankNode) Naming.lookup("rmi://localhost/Goldman_Sachs");
		bankNodeList.add(GoldmanSachs);
		IBankNode JPMorgan = (IBankNode) Naming.lookup("rmi://localhost/JP_Morgan");
		bankNodeList.add(JPMorgan);
		IBankNode Citi = (IBankNode) Naming.lookup("rmi://localhost/Citi");
		bankNodeList.add(Citi);
		IBankNode MerillLynch = (IBankNode) Naming.lookup("rmi://localhost/Merill_Lynch");
		bankNodeList.add(MerillLynch);
		IBankNode HSBC = (IBankNode) Naming.lookup("rmi://localhost/HSBC");
		bankNodeList.add(HSBC);
		IBankNode Barclays = (IBankNode) Naming.lookup("rmi://localhost/Barclays");
		bankNodeList.add(Barclays);
		IBankNode BNP = (IBankNode) Naming.lookup("rmi://localhost/BNP");
		bankNodeList.add(BNP);
		IBankNode DeutscheBank = (IBankNode) Naming.lookup("rmi://localhost/Deutsche_Bank");
		bankNodeList.add(DeutscheBank);

		try {
			for (IBankNode bank : bankNodeList) {
				System.out.println("Name : " + bank.getBankName());
				System.out.println("Id : " + bank.getId());
				System.out.println("\t Neighboors :");
				for (IBankNode neighboor : bank.getNeighboors()) {
					System.out.println("\t Name : " + neighboor.getBankName());
					System.out.println("\t Id : " + neighboor.getId());

				}
			}
		} catch (Exception e) {
			System.err.println(e);
		}

		// On n'enregistre pas les clients pour le moment
/*
		Scanner scanner = new Scanner(System.in);

		System.out.println("Nouveau client ? oui/non");
		String reponse = scanner.nextLine();
		while (!reponse.equals("oui") || !reponse.equals("non")) {
			System.out.println("Ecrire oui ou non");
			reponse = scanner.nextLine();
		}
*/
		
		// Le client est à GoldmannSachs et il veut ouvrir un compte à JPMorgan
		// On attend en retour le numéro du compte
		
		IUser client = (IUser) Naming.lookup("rmi://localhost/client");
		//IUser client = new User("Lebit", "Benj", 23);
		IBankAction action = new AddAccount(client);
		IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), Citi.getId(), EnumMessageType.SINGLE_DEST, null);
		GoldmanSachs.onMessage(message);
		// On aimerait le idAccount
		List<IResult<? extends Serializable>> list = GoldmanSachs.getResultForMessage(messageId);
		for (IResult<? extends Serializable> result : list) {
		}
		messageId++;
		
/*		
		if (reponse.equals("oui")) {
			System.out.println("Nom : ");
			String name = scanner.nextLine();
			System.out.println("Prenom : ");
			String firstName = scanner.nextLine();
			System.out.println("Age : ");
			int age = scanner.nextInt();

			try {
				Registry registry = LocateRegistry.getRegistry(1099);
				client = new User(name, firstName, age);
				String nameFirstName = name + "-" + firstName;
				registry.rebind(nameFirstName, client);
				System.out.println("Client " + nameFirstName + " initialized");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Nom : ");
			String name = scanner.nextLine();
			System.out.println("Prenom : ");
			String firstName = scanner.nextLine();
			String nameFirstName = name + "-" + firstName;
			
			client = (User) Naming.lookup("rmi://localhost/" + nameFirstName);
		}

		System.out.println("Quelle action effectuer ?");
		String action = scanner.nextLine();
		while (!action.equals("ouvrir compte") 
				|| !action.equals("fermer compte")
				|| !action.equals("consulter compte")
				|| !action.equals("retirer")
				|| !action.equals("deposer")
				|| !action.equals("rien")) {
			System.out.println("Choix : ouvrir compte");
			System.out.println("fermer compte");
			System.out.println("consulter compte");
			System.out.println("retirer");
			System.out.println("deposer");
			System.out.println("rien");
			action = scanner.nextLine();
		}
		
		if (action.equals("ouvrir compte")) {
			System.out.println("Quelle banque ?");
			Main.afficherListeBanques(bankNodeList);
			String banqueName = scanner.nextLine();
			// getBank
			// openAccount
			// on retourne le résultat
		} else if (action.equals("fermer compte")) {
			
		} else if (action.equals("consulter compte")) {
			
		} else if (action.equals("retirer")) {
			
		} else if (action.equals("deposer")) {
			
		} else {
			
		}*/
		
		
	}
}
