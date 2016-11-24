import java.rmi.RemoteException;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IBank;
import com.ensimag.api.bank.IUser;

public class Bank implements IBank {
	
	private int bankID;
	private List<IAccount> accountList;

	
	
	public Bank(int bankID) {
		super();
		this.bankID = bankID;
		this.accountList = null;
	}


	public Bank(int bankID, List<IAccount> accountList) {
		super();
		this.bankID = bankID;
		this.accountList = accountList;
	}



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
	public long getBankId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
