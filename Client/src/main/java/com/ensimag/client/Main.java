package com.ensimag.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IBankNode;

public class Main {
	public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
		ArrayList<BankNode> bankNodeList = new ArrayList<BankNode>();
		BankNode SG = (BankNode) Naming.lookup("rmi://localhost/SG");
		bankNodeList.add(SG);
		BankNode BNP = (BankNode) Naming.lookup("rmi://localhost/BNP");
		bankNodeList.add(BNP);
		BankNode Natixis = (BankNode) Naming.lookup("rmi://localhost/Natixis");
		bankNodeList.add(Natixis);

		try {
			for (BankNode bank : bankNodeList) {
				System.out.println("Name : " + bank.getBank());
				System.out.println("Id : " + bank.getId());
				for (IBankNode neighboor : bank.getNeighboors()) {
					System.out.println(((BankNode) neighboor).getBank().getBankName());
				}
			}
			// System.out.println(op.div(33,0));
		} catch (Exception e) {
			System.err.println(e);
		}

	}
}
