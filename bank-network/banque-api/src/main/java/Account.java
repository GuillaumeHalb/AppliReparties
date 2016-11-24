import com.ensimag.api.bank.IAccount;
import com.ensimag.api.bank.IUser;
import com.ensimag.api.bank.NotEnoughMoneyException;

public class Account implements IAccount {

	private IUser user;
	private int number;
	private int balance;
	private int overdraw;
	
	public Account(User user, int number, int balance) {
		this.user = user;
		this.number = number;
		this.balance = balance;
		this.overdraw = 0;
	}
	
	public Account(IUser user2)
	{
		this.user = user2;
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
		if (this.balance + this.overdraw >= amount)
		{
			this.balance -= amount;
			return 0;
		}
		else
		{
			throw new NotEnoughMoneyException(this);
		}
	}

	@Override
	public int getTotal() {
		return this.balance + this.overdraw;
	}

	@Override
	public int setAllowedOverdraw(int overdraw) {
		this.overdraw = overdraw;
		return 0;
	}

}
