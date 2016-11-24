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
// TODO: mettre ça ailleurs
	public static int nodeNumber = 0;

	
	private List<IAccount> accountList;
	private int id;
	
	public BankNode() {
		accountList = new LinkedList<>();
		id = BankNode.nodeNumber;
		BankNode.nodeNumber++;
	}
	
	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		return accountList;
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		for (IAccount account : accountList) {
			if (account.getAccountNumber() == number) {
				return account;
			}
		}
		return null;
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		IAccount account = new Account(user);
		return account;
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		IAccount accountToClose = getAccount(number);
		if (this.accountList.remove(accountToClose)) {
			return true;
		} else {
			if (!this.accountList.contains(accountToClose)) {
				throw new AccountNotFoundException("Account not in this bank");
			}
		}
		return false;
	}

	@Override
	public long getId() throws RemoteException {
		// Numero du proccessus ?		
		return this.nodeNumber;
	}

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
	public void removeNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
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

	@Override
	public void addNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
