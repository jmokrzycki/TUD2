package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.example.shdemo.domain.Rezyser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Car;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class SellingManagerTest {

	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Bolek";
	private final String PIN_1 = "1234";

	private final String NAME_2 = "Lolek";
	private final String PIN_2 = "4321";

	private final String MODEL_1 = "126p";
	private final String MAKE_1 = "Fiat";

	private final String MODEL_2 = "Mondeo";
	private final String MAKE_2 = "Ford";

	@Test
	public void addClientCheck() {

		List<Rezyser> retrievedClients = sellingManager.getAllClients();

		// If there is a client with PIN_1 delete it
		for (Rezyser client : retrievedClients) {
			if (client.getPin().equals(PIN_1)) {
				sellingManager.deleteClient(client);
			}
		}

		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_1);
		rezyser.setPin(PIN_1);
		// ... other properties here

		// Pin is Unique
		sellingManager.addClient(rezyser);

		Rezyser retrievedClient = sellingManager.findClientByPin(PIN_1);

		assertEquals(NAME_1, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
		// ... check other properties here
	}

	@Test
	public void addCarCheck() {

		Car car = new Car();
		car.setMake(MAKE_1);
		car.setModel(MODEL_1);
		// ... other properties here

		Long carId = sellingManager.addNewCar(car);

		Car retrievedCar = sellingManager.findCarById(carId);
		assertEquals(MAKE_1, retrievedCar.getMake());
		assertEquals(MODEL_1, retrievedCar.getModel());
		// ... check other properties here

	}

	@Test
	public void sellCarCheck() {

		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_2);
		rezyser.setPin(PIN_2);

		sellingManager.addClient(rezyser);

		Rezyser retrievedRezyser = sellingManager.findClientByPin(PIN_2);

		Car car = new Car();
		car.setMake(MAKE_2);
		car.setModel(MODEL_2);

		Long carId = sellingManager.addNewCar(car);

		sellingManager.sellCar(retrievedRezyser.getId(), carId);

		List<Car> ownedCars = sellingManager.getOwnedCars(retrievedRezyser);

		assertEquals(1, ownedCars.size());
		assertEquals(MAKE_2, ownedCars.get(0).getMake());
		assertEquals(MODEL_2, ownedCars.get(0).getModel());
	}

	// @Test -
	public void disposeCarCheck() {
		// Do it yourself
	}

}
