package com.ensimag.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.ensimag.services.bank.IBankNode;

public class Main_Simple {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException
    {
    	 ArrayList<IBankNode> bankNodeList = new ArrayList<IBankNode>();
         IBankNode SG = (IBankNode) Naming.lookup("rmi://localhost/SG");
         bankNodeList.add(SG);
         IBankNode BNP = (IBankNode) Naming.lookup("rmi://localhost/BNP");
         bankNodeList.add(BNP);
         IBankNode Natixis = (IBankNode) Naming.lookup("rmi://localhost/Natixis");
         bankNodeList.add(Natixis);
         
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
