package drools.issues;

import drools.issues.model.Person;

class TestData {

	static Person person() {
		var person = new Person();
		person.setAge(20);
		person.setFirstName("John");
		person.setLastName("Doe");
		person.getAddress().setCountry("ES");
		person.getAddress().setNumber("10B");
		person.getAddress().setStreet("Main St");
		person.getAddress().setZipCode("12345");

		return person;
	}
}
