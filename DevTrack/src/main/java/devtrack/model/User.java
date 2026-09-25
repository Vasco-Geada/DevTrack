package devtrack.model;

import devtrack.exception.InvalidEntityException;
import devtrack.utils.MainUtils;

public class User {
	// Attributes
	private final String id;
	private String name;
	private String email;

	// Constructors

	public User(String name, String email) {
		if((name == null || name.isBlank())) {
			throw new InvalidEntityException("name não pode ser null/vazio");
		}
		
		validateEmail(email);
		
		this.id = MainUtils.generateId();
		
		this.name = name;
		this.email = email;
	}

	// Getters
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}

	// Setters
	public void setName(String name) {
		if((name == null || name.isBlank())) {
			throw new InvalidEntityException("name não pode ser null/vazio");
		}
		this.name = name;
	}

	public void setEmail(String email) {
		validateEmail(email);
		
		this.email = email;
	}

	// Methods
	
	public void validateEmail(String email) throws InvalidEntityException {
		
		if((email == null || email.isBlank())) {
			throw new InvalidEntityException("email não pode ser null/vazio");
		}
		if(!(email.contains("@")) || !(email.contains(".")) ) {
			throw new InvalidEntityException("email inválido");
		}
	}
}
