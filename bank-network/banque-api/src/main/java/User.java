import com.ensimag.api.bank.IUser;

public class User implements IUser {
	
	private String name;
	private String firstName;
	private int age;

	
	
	public User(String name, String firstName, int age) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.age = age;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getFirstName() {
		// TODO Auto-generated method stub
		return firstName;
	}

	@Override
	public String getAge() {
		// TODO Auto-generated method stub
		return Integer.toString(age);
	}

}
