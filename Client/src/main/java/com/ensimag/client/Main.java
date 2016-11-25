package com.ensimag.client;

import com.ensimag.services.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException
    {
         IBankNode bkNode = (IBankNode) Naming.lookup("rmi://localhost/1000");
        
        try {
            System.out.println(bkNode.getId());
	    //            System.out.println(op.div(33,0));        
        } catch (Exception e)
        {
            System.err.println(e);
        }

    }
}
