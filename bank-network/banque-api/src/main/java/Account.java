import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IUser;
import com.ensimag.api.bank.NotEnoughMoneyException;

public class Account implements IAccount {

	@Override
	public IUser getAccountUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getAccountNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int add(int amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int remove(int amount) throws NotEnoughMoneyException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int setAllowedOverdraw(int overdraw) {
		// TODO Auto-generated method stub
		return 0;
	}

}
