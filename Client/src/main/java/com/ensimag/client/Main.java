package com.ensimag.client;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.server.impl.AccountBankNode;
import com.ensimag.server.impl.AddAccount;
import com.ensimag.server.impl.AddAmount;
import com.ensimag.server.impl.AuthorizeOverdraw;
import com.ensimag.server.impl.CloseAccount;
import com.ensimag.server.impl.GetAccount;
import com.ensimag.server.impl.Message;
import com.ensimag.server.impl.RemoveAmount;
import com.ensimag.server.impl.Result;
import com.ensimag.server.impl.User;
import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.bank.NotEnoughMoneyException;
import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IResult;
import com.sun.management.GarbageCollectorMXBean;

public class Main {

	private static long messageId = 0;
	private static Scanner scanner;

	public static void afficherListeBanques(ArrayList<IBankNode> bankNodeList) throws RemoteException {
		for (IBankNode bank : bankNodeList) {
			try {
				System.out.println("Nom : " + bank.getBankName());
			} catch (Exception e) {

			}
		}
		System.out.println("All");
	}

	private static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static IUser createUser() throws NumberFormatException, RemoteException {
		scanner = new Scanner(System.in);
		String firstName, lastName, age;
		System.out.println("Saisissez votre Prénom : ");
		firstName = scanner.nextLine();
		System.out.println("Saisissez votre Nom de famille : ");
		lastName = scanner.nextLine();
		System.out.println("Saisissez votre age");
		age = scanner.nextLine();
		while (!Main.tryParseInt(age)) {
			System.out.println("Valeur entière attendu");
			age = scanner.nextLine();
		}
		return new User(lastName, firstName, Integer.parseInt(age));
	}

	private static String chooseAction() {
		scanner = new Scanner(System.in);
		System.out.println("Quelle action effectuer ?");
		System.out.println("Choix :");
		System.out.println("1) ouvrir compte");
		System.out.println("2) fermer compte");
		System.out.println("3) consulter compte");
		System.out.println("4) deposer");
		System.out.println("5) retirer");
		System.out.println("6) autoriser découvert");
		System.out.println("7) sortie");
		String actionString = scanner.nextLine();
		while (!actionString.equals("1") && !actionString.equals("2") && !actionString.equals("3")
				&& !actionString.equals("4") && !actionString.equals("5") && !actionString.equals("6")
				&& !actionString.equals("7")) {
			System.out.println("Choix :");
			System.out.println("1) ouvrir compte");
			System.out.println("2) fermer compte");
			System.out.println("3) consulter compte");
			System.out.println("4) deposer");
			System.out.println("5) retirer");
			System.out.println("6) autoriser découvert");
			System.out.println("7) sortie");
			actionString = scanner.nextLine();
		}
		return actionString;
	}

	private static IBankNode getBankName(ArrayList<IBankNode> bankNodeList) throws RemoteException {
		System.out.println("Quelle banque ?");
		Main.afficherListeBanques(bankNodeList);
		scanner = new Scanner(System.in);
		boolean bankExists = false;
		IBankNode bankNode = null;
		String bankName;
		while (!bankExists) {
			try {
				bankName = scanner.nextLine();
				if (bankName.equals("All"))
					return bankNode;
				bankNode = (IBankNode) Naming.lookup("rmi://localhost/" + bankName);
				bankExists = true;
			} catch (Exception e) {
				System.out.println("La banque spécifiée n'est pas dans la liste");
				System.out.println("Ressaisir un nom de banque : ");
			}
		}
		return bankNode;
	}

	private static IBankMessage createMessage(IBankNode bankNode, IBankAction action, long originalSender)
			throws RemoteException {
		if (bankNode != null)
			return new Message(action, messageId, originalSender, bankNode.getId(), EnumMessageType.SINGLE_DEST, null);
		else
			return new Message(action, messageId, originalSender, 0, EnumMessageType.BROADCAST, null);
	}

	private static void addAccount(ArrayList<IBankNode> bankNodeList, IUser client, IBankNode GoldmanSachs)
			throws RemoteException {
		IBankNode bankNode = getBankName(bankNodeList);
		IBankAction action = new AddAccount(client);
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());
		try {
			GoldmanSachs.onMessage(message);
			List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
			System.out.println("Resultat(s): ");
			for (IResult result : resultList) {
				System.out.println("Compte numero "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountNumber()
						+ " ouvert à la banque " + ((AccountBankNode) (result.getData())).getNode().getBankName()
						+ " pour l'utilisateur "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getFirstName() + " "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getName() + ", "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getAge() + " ans");
				System.out.println("Solde de " + ((AccountBankNode) (result.getData())).getAccount().getTotal());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		messageId++;
	}

	private static void closeAccount(ArrayList<IBankNode> bankNodeList, IBankNode GoldmanSachs) throws RemoteException {
		String accountNumber = getAccountNumber();
		IBankNode bankNode = getBankName(bankNodeList);
		IBankAction action = new CloseAccount(Long.parseLong(accountNumber));
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());
		try {
			GoldmanSachs.onMessage(message);
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
		List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
		System.out.println("Résultat(s) : ");
		int compteur = 0;
		for (IResult result : resultList) {
			try {
				if (((boolean) result.getData())) {
					System.out.println("Fermeture du compte numero " + accountNumber + " effectuée");
				} else {
					System.out.println("Action non effectuée");
				}
			} catch (Exception e) {

			}
		}
		if (compteur == 0) {
			System.out.println("Pas de résultat pour cette action");
		}
		messageId++;
	}

	private static void addAmount(ArrayList<IBankNode> bankNodeList, IBankNode GoldmanSachs) throws RemoteException {
		String accountNumber = getAccountNumber();
		IBankNode bankNode = getBankName(bankNodeList);
		scanner = new Scanner(System.in);

		System.out.println("Combien d'argent voulez vous ajouter");
		String amountToAdd = scanner.nextLine();
		while (!Main.tryParseInt(amountToAdd) || Integer.parseInt(amountToAdd) < 0) {
			System.out.println("Saisissez un entier positif");
			amountToAdd = scanner.nextLine();
		}
		IBankAction action = new AddAmount(Long.parseLong(accountNumber), Integer.parseInt(amountToAdd));
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());
		try {
			GoldmanSachs.onMessage(message);
		} catch (Exception e) {
			System.out.println("action non effectuée");
		}
		List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
		System.out.println("Resultat(s): ");
		int compteur = 0;
		for (IResult result : resultList) {
			try {
				System.out.println("Le solde du compte " + Long.parseLong(accountNumber) + " est maintenant de "
						+ (int) result.getData());
				compteur++;
			} catch (Exception e) {
			}
		}
		if (compteur == 0) {
			System.out.println("Pas de résultat pour cette action");
		}
		messageId++;
	}

	private static void seeAccount(ArrayList<IBankNode> bankNodeList, IBankNode GoldmanSachs) throws RemoteException {
		String accountNumber = getAccountNumber();

		IBankNode bankNode = getBankName(bankNodeList);
		IBankAction action = new GetAccount(Long.parseLong(accountNumber));
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());
		try {
			GoldmanSachs.onMessage(message);
		} catch (Exception e1) {
			System.out.println("Action non effectuée");
		}
		List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
		System.out.println("Resultat(s) : ");
		int compteur = 0;
		for (IResult result : resultList) {
			try {
				System.out.println("Compte numero "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountNumber() + " dans la banque "
						+ ((AccountBankNode) (result.getData())).getNode().getBankName() + " trouvé \n Utilisateur : "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getFirstName() + " "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getName() + ", "
						+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getAge() + " ans");
				System.out.println("Solde de " + ((AccountBankNode) (result.getData())).getAccount().getTotal());
				compteur++;
			} catch (Exception e) {

			}
		}
		if (compteur == 0) {
			System.out.println("Pas de résultat pour cette action");
		}
		messageId++;
	}

	private static void removeAmount(ArrayList<IBankNode> bankNodeList, IBankNode GoldmanSachs) throws RemoteException {
		System.out.println("Quel numéro de compte ?");
		String accountNumber = getAccountNumber();
		while (!Main.tryParseInt(accountNumber)) {
			System.out.println("Saisissez un entier");
		}
		scanner = new Scanner(System.in);
		IBankNode bankNode = getBankName(bankNodeList);

		System.out.println("Combien d'argent voulez vous retirer");
		String amountToRemove = scanner.nextLine();
		while (!Main.tryParseInt(amountToRemove) || Integer.parseInt(amountToRemove) < 0) {
			System.out.println("Saisissez un entier");
			amountToRemove = scanner.nextLine();
		}
		IBankAction action = new RemoveAmount(Long.parseLong(accountNumber), Integer.parseInt(amountToRemove));
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());

		try {
			GoldmanSachs.onMessage(message);
		} catch (Exception e) {
			System.out.println("Action non effectuée");
		}
		List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
		System.out.println("Résultat(s) : ");
		int compteur = 0;
		for (IResult result : resultList) {
			try {
				System.out.println("Le nouveau solde du compte  " + accountNumber + ": " + (int) (result.getData())
						+ " effectuée");
			} catch (Exception e) {

			}
		} 
		if (compteur == 0) {
			System.out.println("Pas de résultat pour cette action");
		}
		messageId++;
	}

	private static void authorizeOverdraw(ArrayList<IBankNode> bankNodeList, IBankNode GoldmanSachs)
			throws RemoteException {
		String accountNumber = getAccountNumber();
		IBankNode bankNode = getBankName(bankNodeList);
		scanner = new Scanner(System.in);
		System.out.println("A combien voulez vous autoriser votre découvert ?");
		String amountForOverdraw = scanner.nextLine();
		while (!Main.tryParseInt(amountForOverdraw) || Integer.parseInt(amountForOverdraw) < 0) {
			System.out.println("Saisissez un entier");
			amountForOverdraw = scanner.nextLine();
		}
		IBankAction action = new AuthorizeOverdraw(Long.parseLong(accountNumber), Integer.parseInt(amountForOverdraw));
		IBankMessage message = createMessage(bankNode, action, GoldmanSachs.getId());

		try {
			GoldmanSachs.onMessage(message);
		} catch (Exception e) {
			System.out.println("Action non effectuée");
		}
		List<IResult<? extends Serializable>> resultList = GoldmanSachs.getResultForMessage(message.getMessageId());
		System.out.println("Résultat(s) :");
		int compteur = 0;
		for (IResult result : resultList) {
			try {
				System.out.println("Le découvert du compte  " + accountNumber + " est maintenant de "
						+ (int) (result.getData()) + " ,effectuée");

			} catch (Exception e) {

			}
		}
		if (compteur == 0) {
			System.out.println("Pas de résultat pour cette action");
		}
		messageId++;
	}

	private static String getAccountNumber() {
		System.out.println("Quel numéro de compte ?");
		scanner = new Scanner(System.in);
		String accountNumber = scanner.nextLine();
		while (!Main.tryParseInt(accountNumber)) {
			System.out.println("Saisissez un entier");
		}
		return accountNumber;
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

		// IUser client = new User(lastName, firstName, Integer.parseInt(age));
		IUser client = (IUser) Naming.lookup("rmi://localhost/client");
		boolean exit = false;
		// client = createUser();

		while (!exit) {

			String actionString = chooseAction();

			if (actionString.equals("1")) {
				addAccount(bankNodeList, client, GoldmanSachs);
			} else if (actionString.equals("2")) {
				closeAccount(bankNodeList, GoldmanSachs);
			} else if (actionString.equals("3")) {
				seeAccount(bankNodeList, GoldmanSachs);
			} else if (actionString.equals("4")) {
				addAmount(bankNodeList, GoldmanSachs);
			} else if (actionString.equals("5")) {
				removeAmount(bankNodeList, GoldmanSachs);
			} else if (actionString.equals("6")) {
				authorizeOverdraw(bankNodeList, GoldmanSachs);
			} else if (actionString.equals("7")) {
				System.out.println("Sortie du programme...");
				exit = true;
			} else {

			}
		}
		System.out.println("sortie");
	}
}
