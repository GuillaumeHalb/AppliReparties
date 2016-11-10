import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IUser;
import com.ensimag.api.bank.NotEnoughMoneyException;

public class Account implements IAccount {

	private User user;
	private int number;
	private int balance;
	
	public Account(User user, int number, int balance) {
		this.user = user;
		this.number = number;
		this.balance = balance;
	}
	
	public Account(User user)
	{
		this.user = user;
		//TODO number
		this.balance = 0;
	}
	
	@Override
	public IUser getAccountUser() {
		return this.user;
	}

	@Override
	public long getAccountNumber() {
		return this.number;
	}

	@Override
	public int add(int amount) {
		this.balance += amount;
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
