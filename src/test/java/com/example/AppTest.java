package com.example;

import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_BOTH_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DDL_GENERATION_MODE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.DROP_AND_CREATE;
import static org.eclipse.persistence.config.PersistenceUnitProperties.TRANSACTION_TYPE;

import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;

public class AppTest {
	
	/*
<?xml version="1.0" encoding="UTF-8" ?>
<persistence
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
	version="2.0" xmlns="http://java.sun.com/xml/ns/persistence">
	<persistence-unit name="example"
		transaction-type="RESOURCE_LOCAL">
		<provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
		<class>com.example.Person</class>
		<properties>
			<property name="javax.persistence.jdbc.driver"
				value="org.sqlite.JDBC" />
			<property name="javax.persistence.jdbc.url"
				value="jdbc:sqlite:./test.db" />
			<property name="eclipselink.logging.level" value="ALL" />
			<property name="eclipselink.ddl-generation"
				value="drop-and-create-tables" /> <!-- create-tables -->
		</properties>
	</persistence-unit>
</persistence>
	 */

	private static EntityManager em;
	
	@Test
	void testJpa() throws Exception {
		
		Properties props = new Properties();
		
		props.put(TRANSACTION_TYPE, "RESOURCE_LOCAL");
		props.put("provider", "org.eclipse.persistence.jpa.PersistenceProvider");
		props.put("class", "com.example.Person");
		props.put("javax.persistence.jdbc.driver", "org.sqlite.JDBC");
		props.put("javax.persistence.jdbc.url", "jdbc:sqlite:./test.db");
		props.put("eclipselink.logging.level", "ALL");
		
		props.put(DDL_GENERATION, DROP_AND_CREATE);
		props.put(DDL_GENERATION_MODE, DDL_BOTH_GENERATION);
		//props.put(CREATE_JDBC_DDL_FILE, "create.sql");
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("example", props);
		em = emf.createEntityManager();

		createPerson(1, "Ravi", "Raj", 33);
		createPerson(2, "Amit", "Raj", 55);
		createPerson(3, "Nitish", "Kumar", 40);
		
		// Fetch them
		TypedQuery<Person> q = em.createQuery("select p from Person p", Person.class);
		List<Person> persons = q.getResultList();
		for (Person p : persons) {
			System.out.println(p.getId() + ", " + p.getLastName() + ", " + p.getFirstName() + ", " + p.getAge());
		}
		
		// Close the entity manager
		em.close();
		emf.close();
	}

	private static void createPerson(int id, String firstName, String lastName, int age) {
		em.getTransaction().begin();
		Person p = new Person(id, firstName, lastName, age);
		em.persist(p);
		em.getTransaction().commit();
	}
}
