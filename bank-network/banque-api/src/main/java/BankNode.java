import java.io.Serializable;
import java.rmi.RemoteException;
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

	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getId() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		// TODO Auto-generated method stub

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
