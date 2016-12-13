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
import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.bank.NotEnoughMoneyException;
import com.ensimag.services.message.EnumMessageType;
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
		System.out.println("All");
	}

	static boolean tryParseInt(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static IBankNode getBankName(ArrayList<IBankNode> bankNodeList) throws RemoteException {
		System.out.println("Quelle banque ?");
		Main.afficherListeBanques(bankNodeList);

		Scanner scanner = new Scanner(System.in);
		boolean bankExists = false;
		IBankNode bankNode = null;
		while (!bankExists) {
			try {
				String bankName = scanner.nextLine();
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

		// On n'enregistre pas les clients pour le moment
		/*
		 * Scanner scanner = new Scanner(System.in);
		 * 
		 * System.out.println("Nouveau client ? oui/non"); String reponse =
		 * scanner.nextLine(); while (!reponse.equals("oui") ||
		 * !reponse.equals("non")) { System.out.println("Ecrire oui ou non");
		 * reponse = scanner.nextLine(); }
		 */

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

			if (actionString.equals("1")) {
				IBankNode bankNode = null;

				/*
				 * System.out.println("Quelle banque ?");
				 * Main.afficherListeBanques(bankNodeList);
				 * 
				 * boolean bankExists = false; IBankNode bankNode = null; while
				 * (!bankExists) { try { String bankName = scanner.nextLine();
				 * bankNode = (IBankNode) Naming.lookup("rmi://localhost/" +
				 * bankName); bankExists = true; } catch (Exception e) {
				 * System.out.println(
				 * "La banque spécifiée n'est pas dans la liste");
				 * System.out.println("Ressaisir un nom de banque : "); } }
				 */
				bankNode = getBankName(bankNodeList);
				IBankAction action = new AddAccount(client);
				IBankMessage message = null;
				if (bankNode != null) {
					message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
							EnumMessageType.SINGLE_DEST, null);
				} else {
					message = new Message(action, messageId, GoldmanSachs.getId(), 0,
							EnumMessageType.BROADCAST, null);
				}
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
					for (IResult result : resultList) {
						System.out.println("Resultat: ");
						System.out.println("Compte numero " + ((AccountBankNode) (result.getData())).getAccount().getAccountNumber()
								+ " ouvert à la banque " + ((AccountBankNode) (result.getData())).getNode().getBankName() + " pour l'utilisateur "
								+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getFirstName() + " "
								+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getName() + ", "
								+ ((AccountBankNode) (result.getData())).getAccount().getAccountUser().getAge() + " ans");
						System.out.println("Solde de " + ((AccountBankNode) (result.getData())).getAccount().getTotal());
					}

					messageId++;
				} catch (Exception e) {
					e.printStackTrace();
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
					if (e instanceof NotEnoughMoneyException) {
						System.out.println(e.toString());
					} else if (e instanceof AccountNotFoundException) {
						System.out.println(e.toString()); // "Account not found
															// " + accountNumber
					}
					System.out.println(e.getMessage());
				}

			} else if (actionString.equals("3")) {
				System.out.println("Quel numéro de compte ?");
				String accountNumber = scanner.nextLine();
				while (!Main.tryParseInt(accountNumber)) {
					System.out.println("Saisissez un entier");
					accountNumber = scanner.nextLine();
				}

				System.out.println("Quelle banque ?");
				Main.afficherListeBanques(bankNodeList);

				boolean bankExists = false;
				IBankNode bankNode = null;
				String bankName = null;
				while (!bankExists) {
					try {
						bankName = scanner.nextLine();
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
					System.out.println("action non effectuée");
				}

			} else if (actionString.equals("4")) {
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
				System.out.println("Combien d'argent voulez vous ajouter");
				String amountToAdd = scanner.nextLine();
				while (!Main.tryParseInt(amountToAdd) || Integer.parseInt(amountToAdd) < 0) {
					System.out.println("Saisissez un entier positif");
					amountToAdd = scanner.nextLine();
				}
				IBankAction action = new AddAmount(Long.parseLong(accountNumber), Integer.parseInt(amountToAdd));
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);
				Result resultFromRequest = new Result(client, message.getMessageId());
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());

					for (IResult result : resultList) {
						System.out.println("Resultat: ");
						System.out.println("Le solde du compte " + Long.parseLong(accountNumber) + " est maintenant de "
								+ (int) result.getData());
					}
					messageId++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (actionString.equals("5")) {
				System.out.println("Quel numéro de compte ?");
				String accountNumber = scanner.nextLine();
				while (!Main.tryParseInt(accountNumber)) {
					System.out.println("Saisissez un entier");
				}
				IBankNode bankNode = getBankName(bankNodeList);

				System.out.println("Combien d'argent voulez vous retirer");
				String amountToRemove = scanner.nextLine();
				while (!Main.tryParseInt(amountToRemove) || Integer.parseInt(amountToRemove) < 0) {
					System.out.println("Saisissez un entier");
					amountToRemove = scanner.nextLine();
				}
				IBankAction action = new RemoveAmount(Long.parseLong(accountNumber), Integer.parseInt(amountToRemove));
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);

				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
					for (IResult result : resultList) {
						System.out.println("Le nouveau solde du compte  " + accountNumber + ": "
								+ (int) (result.getData()) + " effectuée");

					}
					messageId++;
				} catch (Exception e) {
					System.out.println("Action non effectuée");
				}
			} else if (actionString.equals("6")) {
				System.out.println("Quel numéro de compte ?");
				String accountNumber = scanner.nextLine();
				while (!Main.tryParseInt(accountNumber)) {
					System.out.println("Saisissez un entier");
				}
				IBankNode bankNode = getBankName(bankNodeList);

				System.out.println("A combien voulez vous autoriser votre découvert ?");
				String amountForOverdraw = scanner.nextLine();
				while (!Main.tryParseInt(amountForOverdraw) || Integer.parseInt(amountForOverdraw) < 0) {
					System.out.println("Saisissez un entier");
					amountForOverdraw = scanner.nextLine();
				}
				IBankAction action = new AuthorizeOverdraw(Long.parseLong(accountNumber),
						Integer.parseInt(amountForOverdraw));
				IBankMessage message = new Message(action, messageId, GoldmanSachs.getId(), bankNode.getId(),
						EnumMessageType.SINGLE_DEST, null);

				Result resultFromRequest = new Result(client, message.getMessageId());
				try {
					GoldmanSachs.onMessage(message);
					List<IResult<? extends Serializable>> resultList = GoldmanSachs
							.getResultForMessage(message.getMessageId());
					for (IResult result : resultList) {
						System.out.println("Le découvert du compte  " + accountNumber + " est maintenant de "
								+ (int) (result.getData()) + " ,effectuée");

					}
					messageId++;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (actionString.equals("7")) {
				System.out.println("Sortie du programme...");
				exit = true;
			} else {

			}
		}
	}
}
