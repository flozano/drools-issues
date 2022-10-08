package drools.issues.model;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class Person {

	private String firstName;
	private String lastName;
	private int age;

	private final Counter visits = new Counter();

	private final Address address = new Address();

	private Collection<Long> favoriteNumbers = Collections.emptyList();

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

	public Collection<Long> getFavoriteNumbers() {
		return favoriteNumbers;
	}

	public void setFavoriteNumbers(Collection<Long> favoriteNumbers) {
		this.favoriteNumbers = favoriteNumbers;
	}

	public void setFavoriteNumbers(Long... values) {
		this.favoriteNumbers = values == null ? null : Sets.newHashSet(values);
	}

	public void setFavoriteNumbers(String... values) {
		this.favoriteNumbers = values == null ? null
				: Sets.newHashSet(values).stream().map(Long::parseLong).collect(Collectors.toSet());
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
