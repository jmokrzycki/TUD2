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

import com.example.shdemo.domain.Film;

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

	private final String TYTUL_1 = "126p";
	private final String GATUNEK_1 = "Fiat";

	private final String TYTUL_2 = "Mondeo";
	private final String GATUNEK_2 = "Ford";

	@Test
	public void addFilmCheck() {

		List<Film> retrievedFilms = sellingManager.getAllFilm();

		// If there is a client with PIN_1 delete it
		for (Film client : retrievedFilms) {
			if (client.getTytul().equals(TYTUL_1)) {
				sellingManager.deleteFilm(client);
			}
		}

		Film film = new Film();
		film.setTytul(TYTUL_1);
		film.setGatunek(GATUNEK_1);
		// ... other properties here

		// Pin is Unique
		sellingManager.addFilm(film);

		Film retrievedClient = sellingManager.findFilmByTytul(TYTUL_1);

		assertEquals(TYTUL_1, retrievedClient.getTytul());
		assertEquals(GATUNEK_1, retrievedClient.getGatunek());
		// ... check other properties here
	}

	@Test
	public void addClientCheck() {

		List<Rezyser> retrievedClients = sellingManager.getAllRezyzser();

		// If there is a client with PIN_1 delete it
		for (Rezyser client : retrievedClients) {
			if (client.getPin().equals(PIN_1)) {
				sellingManager.deleteRezyser(client);
			}
		}

		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_1);
		rezyser.setPin(PIN_1);
		// ... other properties here

		// Pin is Unique
		sellingManager.addRezyser(rezyser);

		Rezyser retrievedClient = sellingManager.findRezyserByPin(PIN_1);

		assertEquals(NAME_1, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
		// ... check other properties here
	}

	@Test
	public void addCarCheck() {

		Film film = new Film();
		film.setTytul(GATUNEK_1);
		film.setGatunek(TYTUL_1);
		// ... other properties here

		Long carId = sellingManager.addFilm(film);

		Film retrievedFilm = sellingManager.findRezyserById(carId);
		assertEquals(GATUNEK_1, retrievedFilm.getTytul());
		assertEquals(TYTUL_1, retrievedFilm.getGatunek());
		// ... check other properties here

	}

	@Test
	public void sellCarCheck() {

		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_2);
		rezyser.setPin(PIN_2);

		sellingManager.addRezyser(rezyser);

		Rezyser retrievedRezyser = sellingManager.findRezyserByPin(PIN_2);

		Film film = new Film();
		film.setTytul(GATUNEK_2);
		film.setGatunek(TYTUL_2);

		Long carId = sellingManager.addFilm(film);

		sellingManager.sellCar(retrievedRezyser.getId(), carId);

		List<Film> ownedFilms = sellingManager.getOwnedCars(retrievedRezyser);

		assertEquals(1, ownedFilms.size());
		assertEquals(GATUNEK_2, ownedFilms.get(0).getTytul());
		assertEquals(TYTUL_2, ownedFilms.get(0).getGatunek());
	}

	// @Test -
	public void disposeCarCheck() {
		// Do it yourself
	}

}
