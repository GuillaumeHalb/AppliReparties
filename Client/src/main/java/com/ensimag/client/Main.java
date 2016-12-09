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
import com.ensimag.server.impl.CloseAccount;
import com.ensimag.server.impl.GetAccount;
import com.ensimag.server.impl.Message;
import com.ensimag.server.impl.Result;
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
			try {
				System.out.println("Nom : " + bank.getBankName());
			} catch (Exception e) {

			}
		}
	}

	static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
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

		// try {
		// for (IBankNode bank : bankNodeList) {
		// System.out.println("Name : " + bank.getBankName());
		// System.out.println("Id : " + bank.getId());
		// System.out.println("\t Neighboors :");
		// for (IBankNode neighboor : bank.getNeighboors()) {
		// System.out.println("\t Name : " + neighboor.getBankName());
		// System.out.println("\t Id : " + neighboor.getId());
		//
		// }
		// }
		// } catch (Exception e) {
		// System.err.println(e);
		// }

		// On n'enregistre pas les clients pour le moment
		/*
		 * Scanner scanner = new Scanner(System.in);
		 * 
		 * System.out.println("Nouveau client ? oui/non"); String reponse =
		 * scanner.nextLine(); while (!reponse.equals("oui") ||
		 * !reponse.equals("non")) { System.out.println("Ecrire oui ou non");
		 * reponse = scanner.nextLine(); }
		 */

		// ********************Premier
		// Test****************************************************************************************/
		// Le client est à GoldmannSachs et il veut ouvrir un compte à JPMorgan
		// On attend en retour le numéro du compte

		// IUser client = (IUser) Naming.lookup("rmi://localhost/client");
		// IUser client = new User("Lebit", "Benj", 23);
		// IBankAction actiontest = new AddAccount(client);
		// IBankMessage messagetest = new Message(actiontest, messageId,
		// GoldmanSachs.getId(), Citi.getId(),
		// EnumMessageType.SINGLE_DEST, null);
		// Result resultRequesttest = new Result(client,
		// messagetest.getMessageId());
		// try {
		// GoldmanSachs.onMessage(messagetest);
		// List<IResult<? extends Serializable>> list =
		// GoldmanSachs.getResultForMessage(messagetest.getMessageId());
		// for (IResult result : list) {
		// System.out.println("Result: " + ((IAccount)
		// result.getData()).getAccountUser().getFirstName());
		// }
		// // On aimerait le idAccount
		// messageId++;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// System.out.println(e.getMessage());
		// }

		// ****************************************************************************************************************/

		/*
		 * if (reponse.equals("oui")) { System.out.println("Nom : "); String
		 * name = scanner.nextLine(); System.out.println("Prenom : "); String
		 * firstName = scanner.nextLine(); System.out.println("Age : "); int age
		 * = scanner.nextInt();
		 * 
		 * try { Registry registry = LocateRegistry.getRegistry(1099); client =
		 * new User(name, firstName, age); String nameFirstName = name + "-" +
		 * firstName; registry.rebind(nameFirstName, client);
		 * System.out.println("Client " + nameFirstName + " initialized"); }
		 * catch (Exception e) { e.printStackTrace(); } } else {
		 * System.out.println("Nom : "); String name = scanner.nextLine();
		 * System.out.println("Prenom : "); String firstName =
		 * scanner.nextLine(); String nameFirstName = name + "-" + firstName;
		 * 
		 * client = (User) Naming.lookup("rmi://localhost/" + nameFirstName); }
		 */

		IUser client = (IUser) Naming.lookup("rmi://localhost/client");
		boolean exit = false;
		Scanner scanner = new Scanner(System.in);
		while (!exit) {

			System.out.println("Quelle action effectuer ?");
			System.out.println("Choix :");
			System.out.println("1) ouvrir compte");
			System.out.println("2) fermer compte");
			System.out.println("3) consulter compte");
			System.out.println("4) retirer");
			System.out.println("5) deposer");
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
				System.out.println("4) retirer");
				System.out.println("5) deposer");
				System.out.println("6) autoriser découvert");
				System.out.println("7) sortie");
				actionString = scanner.nextLine();
			}

			if (actionString.equals("1")) {
				System.out.println("Quelle banque ?");
				Main.afficherListeBanques(bankNodeList);

				boolean bankExists = false;
				IBankNode bankNode = null;
				while (!bankExists) {
					try {
						String bankName = scanner.nextLine();
						bankNode = (IBankNode) Naming.lookup("rmi://localhost/" + bankName);
						bankExists = true;
					} catch (Exception e) {
						System.out.println("La banque spécifiée n'est pas dans la liste");
						System.out.println("Ressaisir un nom de banque : ");
					}
				}
				IBankAction action = new AddAccount(client);
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);
				Result resultFromRequest = new Result(client, message.getMessageId());
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
					for (IResult result : resultList) {
						System.out.println("Resultat: ");
						System.out.println("Compte numero " + ((IAccount) result.getData()).getAccountNumber()
								+ " ouvert pour l'utilisateur"
								+ ((IAccount) result.getData()).getAccountUser().getFirstName() + " "
								+ ((IAccount) result.getData()).getAccountUser().getName() + ", "
								+ ((IAccount) result.getData()).getAccountUser().getAge() + " ans");
						System.out.println("Solde de " + ((IAccount) result.getData()).getTotal());
					}

					messageId++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (actionString.equals("2")) {
				System.out.println("Quel numéro de compte ?");
				String accountNumber = scanner.nextLine();
				while (!Main.tryParseInt(accountNumber)) {
					System.out.println("Saisissez un entier");
				}

				System.out.println("Quelle banque ?");
				Main.afficherListeBanques(bankNodeList);

				boolean bankExists = false;
				IBankNode bankNode = null;
				while (!bankExists) {
					try {
						String bankName = scanner.nextLine();
						bankNode = (IBankNode) Naming.lookup("rmi://localhost/" + bankName);
						bankExists = true;
					} catch (Exception e) {
						System.out.println("La banque spécifiée n'est pas dans la liste");
						System.out.println("Resaisir un nom de banque : ");
					}
				}
				IBankAction action = new CloseAccount(Long.parseLong(accountNumber));
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);
				Result resultFromRequest = new Result(client, message.getMessageId());
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
					for (IResult result : resultList) {
						if (((boolean) result.getData())) {
							System.out.println("Fermeture du compte numero " + accountNumber + " effectuée");
						} else {
							System.out.println("Action non effectuée");
						}
					}
					messageId++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			} else if (actionString.equals("3")) {
				System.out.println("Quel numéro de compte ?");
				String accountNumber = scanner.nextLine();
				while (!Main.tryParseInt(accountNumber)) {
					System.out.println("Saisissez un entier");
				}

				System.out.println("Quelle banque ?");
				Main.afficherListeBanques(bankNodeList);

				boolean bankExists = false;
				IBankNode bankNode = null;
				while (!bankExists) {
					try {
						String bankName = scanner.nextLine();
						bankNode = (IBankNode) Naming.lookup("rmi://localhost/" + bankName);
						bankExists = true;
					} catch (Exception e) {
						System.out.println("La banque spécifiée n'est pas dans la liste");
						System.out.println("Resaisir un nom de banque : ");
					}
				}
				IBankAction action = new GetAccount(Long.parseLong(accountNumber));
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);
				Result resultFromRequest = new Result(client, message.getMessageId());
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
										
					for (IResult result : resultList) {
						System.out.println("Resultat: ");
						System.out.println("Compte numero " + ((IAccount) result.getData()).getAccountNumber()
								+ " trouvé \n Utilisateur : "
								+ ((IAccount) result.getData()).getAccountUser().getFirstName() + " "
								+ ((IAccount) result.getData()).getAccountUser().getName() + ", "
								+ ((IAccount) result.getData()).getAccountUser().getAge() + " ans");
						System.out.println("Solde de " + ((IAccount) result.getData()).getTotal());
					}
					messageId++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			} else if (actionString.equals("4")) {

			} else if (actionString.equals("5")) {

			} else if (actionString.equals("6")) {

			} else if (actionString.equals("7")) {
				System.out.println("Sortie du programme...");
				exit = true;
			} else {

			}
		}
	}
}
