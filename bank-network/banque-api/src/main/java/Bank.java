import java.rmi.RemoteException;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBank;
import com.ensimag.api.bank.IUser;

public class Bank implements IBank {
	
	private List<IAccount> accountList;

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
	public long getBankId() {
		// TODO Auto-generated method stub
		return 0;
	}

}