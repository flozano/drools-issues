package drools.issues.model;

public class Person {

	private String firstName;
	private String lastName;
	private int age;

	private final Counter visits = new Counter();

	private final Address address = new Address();

	public Counter getVisits() {
		return visits;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public static class Address {

		private String street;
		private String number;
		private String zipCode;
		private String country;
		private final Counter visitors = new Counter();

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		public Counter getVisitors() {
			return visitors;
		}

	}

}
