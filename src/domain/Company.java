package domain;

import java.time.LocalDate;

public class Company {

	private String name;
	private String address;
	private String phoneNumber;
	private LocalDate registrationDate;

	public Company(String name, String address, String phoneNumber) {
		throw new UnsupportedOperationException();
	}

	public int giveCompanySeniority() {
		throw new UnsupportedOperationException();
	}
}
