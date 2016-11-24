import com.ensimag.api.bank.IUser;

public class User implements IUser {

	private static final long serialVersionUID = 3355610152396539315L;
	
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
		return name;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getAge() {
		return Integer.toString(age);
	}

}
