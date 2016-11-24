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

	private static final long serialVersionUID = 1L;
	
	private Bank bank;
	private int id;
	private List<INode<IBankMessage>> neighboors;
	
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
		return this.bank.getAccount(number);
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
	
	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		if (message.getDestinationBankId() == this.bank.getBankId()) {
			try {
				message.getAction().execute(this);
			} catch (Exception exception) {
				throw new RemoteException("Error while executing message action");
			}
		} else {
			message.setSenderId(this.id);
			for (INode<IBankMessage> neighboor : this.neighboors) {
				neighboor.onMessage(message);
			}
		}
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
