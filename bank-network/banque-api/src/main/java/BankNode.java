import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBankMessage;
import com.ensimag.api.bank.IBankNode;
import com.ensimag.api.bank.IUser;
import com.ensimag.api.message.IAck;
import com.ensimag.api.message.IResult;
import com.ensimag.api.node.INode;

public class BankNode implements IBankNode {		
	private Bank bank;
	private int id;
	private List<INode> neighboors;
	
	public BankNode(Bank bank, int id) {
		this.bank = bank;
		this.id = id;
		this.neighboors = new LinkedList<>();
	}
	
	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		return this.bank.getAccounts();
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		return this.bank.getAccout(number);
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {		
		return this.bank.openAccount(user);
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		return this.bank.closeAccount(number);
	}

	@Override
	public long getId() throws RemoteException {
		return this.id;
	}

	@Override
	public void addNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		this.neighboors.add(neighboor);
	}
	
	@Override
	public void removeNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		this.neighboors.remove(neighboor);
	}
	
// Implement message before implemented those methods
	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		// Check if the message is for this bank
		/*if (message.getRecipientId() == this.nodeNumber) {
			// Execute the action
		} else {
			// Transfer the message to the neighboor
		}*/
	}
	
	@Override
	public void onAck(IAck ack) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<IResult<? extends Serializable>> getResultForMessage(long messageId) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deliverResult(IResult<Serializable> result) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}
