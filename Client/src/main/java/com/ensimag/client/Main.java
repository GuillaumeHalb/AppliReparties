package com.ensimag.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.ensimag.server.impl.Bank;
import com.ensimag.server.impl.BankNode;
import com.ensimag.services.bank.IBankNode;

public class Main {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException
    {
    	 ArrayList<IBankNode> bankNodeList = new ArrayList<IBankNode>();
         IBankNode GoldmanSachs = (IBankNode) Naming.lookup("rmi://localhost/Goldman_Sachs");
         bankNodeList.add( GoldmanSachs);
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
	            for(IBankNode bank : bankNodeList)
	            {
	           	 System.out.println("Name : " + bank.getBankName());
	           	 System.out.println("Id : " + bank.getId());
	           	 System.out.println("\t Neighboors :");
	           	 for (IBankNode neighboor : bank.getNeighboors())
	           	 	{
	           		 System.out.println("\t Name : " + neighboor.getBankName());
	           		 System.out.println("\t Id : " + neighboor.getId());
	           		 
	           	 	}
	            }
        } catch (Exception e)
        {
            System.err.println(e);
        }
	}
}
