package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import com.example.shdemo.domain.Rezyser;
import org.junit.After;
import org.junit.Before;
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
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = false)
@Transactional
public class SellingManagerTest {

	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Bolek";
	private final String PIN_1 = "1234";

	private final String NAME_2 = "Lolek";
	private final String PIN_2 = "Jan-Kowalski";

	private final String TYTUL_1 = "Akcji1";
	private final String GATUNEK_1 = "Akcja";

	private final String TYTUL_2 = "Akcji2";
	private final String GATUNEK_2 = "Dramat";

	private List<Long>  istniejaceFilmy  = new ArrayList<Long>();
	private List<Long>  istniejacyRezyserzy = new ArrayList<Long>();

	@Before
	public void fillDatabase(){
		List<Film> filmy = sellingManager.getAllFilm();
		List<Rezyser> rezyserzy = sellingManager.getAllRezyzser();

		for(Film f : filmy)
			istniejaceFilmy.add(f.getId());

		for(Rezyser r : rezyserzy)
			istniejacyRezyserzy.add(r.getId());


		Film film1  = new Film();
		film1.setTytul("PierwszyTytul");
		film1.setGatunek("PierwszyGatunek");
		sellingManager.addFilm(film1);
		Film film2  = new Film();
		film2.setTytul("DrugiTytul");
		film2.setGatunek("DrugiGatunek");
		sellingManager.addFilm(film2);
		Film film3  = new Film();
		film3.setTytul("TrzeciTytul");
		film3.setGatunek("TrzeciGatunek");
		sellingManager.addFilm(film3);
		Rezyser rezyser1 = new Rezyser();
		rezyser1.setFirstName("PierwszeImie");
		rezyser1.setPin("PierwszyPin");
		sellingManager.addRezyser(rezyser1);
		Rezyser rezyser2 = new Rezyser();
		rezyser2.setFirstName("DrugieImie");
		rezyser2.setPin("DrugiPin");
		sellingManager.addRezyser(rezyser2);
		Rezyser rezyser3 = new Rezyser();
		rezyser3.setFirstName("TrzecieImie");
		rezyser3.setPin("TrzeciPin");
		sellingManager.addRezyser(rezyser3);
	}

	@After
	public void clearDatabase(){
		List<Film> noweFilmy = sellingManager.getAllFilm();
		List<Rezyser> nowiRezyserzy = sellingManager.getAllRezyzser();

		boolean usun;

		for(Film f : noweFilmy) {
			usun = true;
			for (Long f1 : istniejaceFilmy)
				if (f.getId() == f1) {
					usun = false;
					break;
				}
			if(usun)
				sellingManager.deleteFilm(f);
		}

		for(Rezyser r : nowiRezyserzy) {
			usun = true;
			for (Long r1 : istniejacyRezyserzy)
				if (r.getId() == r1)
				{
					usun = false;
					break;
				}
			if(usun)
				sellingManager.deleteRezyser(r);
		}
	}
	@Test
	public void addFilmCheck() {
		List<Film> retrievedFilms = sellingManager.getAllFilm();

		// If there is a client with PIN_1 delete it
		for (Film film : retrievedFilms) {
			if (film.getTytul().equals(TYTUL_1)) {
				sellingManager.deleteFilm(film);
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
	public void addRezyserCheck() {

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
	public void assignRezyserToFilmCheck() {
		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_2);
		rezyser.setPin(PIN_2);

		sellingManager.addRezyser(rezyser);

		Film film = new Film();
		film.setTytul(GATUNEK_2);
		film.setGatunek(TYTUL_2);

		Long retrievedFilmId = sellingManager.addFilm(film);

		Rezyser retrievedRezyser = sellingManager.findRezyserByPin(PIN_2);
		sellingManager.assignRezyserToFilm(retrievedRezyser.getId(), retrievedFilmId);

		Rezyser retrievedRezyser2 = sellingManager.findRezyserByPin(PIN_2);

		List<Film> retrievedFilm = retrievedRezyser2.getFilms();

		boolean assignedRezyserToFilm = false;
		for(Film f : retrievedFilm){
			if(f.getId() == retrievedFilmId){
				assignedRezyserToFilm = true;
			}
		}

		assertEquals(true, assignedRezyserToFilm);

		//usuniecie listy filmow z testowego rezysera (teraz mozna usuwac rezysera, nie ma relacji do filmow)
		retrievedRezyser2.removaAllFilmsFromRezyser();
	}

	// @Test -
	public void disposeCarCheck() {
		// Do it yourself
	}

	//do starego

	@Test
	public void deleteRezyserCheck() {
		//sprawdzenie czy istnieja jakies rekory przed usunieciem
		//zapisanie stanu bazy do porownania przed usunieciem
		List<Rezyser> rekordyPrzedAktualizacja = sellingManager.getAllRezyzser();

		int iloscRekordowPrzedUsunieciem = rekordyPrzedAktualizacja.size();

		//dodanie testowego rezysera
		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_1);
		rezyser.setPin(PIN_1);

		sellingManager.addRezyser(rezyser);

		sellingManager.deleteRezyser(rezyser);

		Rezyser retrievedClient = sellingManager.findRezyserByPin(PIN_1);

		assertEquals(null, retrievedClient);

		boolean brakujacyRekord = false;
		//porownanie bazy po aktualizacji z przed, jesli byly wczesniej dane
		if(iloscRekordowPrzedUsunieciem > 0){
			for(Rezyser r : rekordyPrzedAktualizacja){
				if (sellingManager.findRezyserById(r.getId()) == null) {
					brakujacyRekord = true;
					break;
				}
			}
		}

		assertEquals(false, brakujacyRekord);
	}

	//nowe


	@Test
	public void updateFilmCheck() {
		//dodanie testowego filmu
		Film film = new Film();
		film.setTytul(TYTUL_1);
		film.setGatunek(GATUNEK_1);

		sellingManager.addFilm(film);

		//zmiana nazwy
		film.setTytul(TYTUL_2);
		sellingManager.updateFilm(film);

		Film retrievedClient = sellingManager.findFilmByTytul(TYTUL_2);

		assertEquals(TYTUL_2, retrievedClient.getTytul());
		assertEquals(GATUNEK_1, retrievedClient.getGatunek());
	}

	@Test
	public void updateRezyserCheck() {
		//dodanie testowego rezysera
		Rezyser rezyser = new Rezyser();
		rezyser.setFirstName(NAME_1);
		rezyser.setPin(PIN_1);

		sellingManager.addRezyser(rezyser);

		//zmiana nazwy
		rezyser.setFirstName(NAME_2);
		sellingManager.updateRezyser(rezyser);

		Rezyser retrievedClient = sellingManager.findRezyserByPin(PIN_1);

		assertEquals(NAME_2, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
	}

	@Test
	public void deleteFilmCheck() {
		List<Film> rekordyPrzedAktualizacja = sellingManager.getAllFilm();

		int iloscRekordowPrzedUsunieciem = rekordyPrzedAktualizacja.size();

		//dodanie testowego filmu
		Film film = new Film();
		film.setTytul(TYTUL_1);
		film.setGatunek(GATUNEK_1);

		sellingManager.addFilm(film);

		sellingManager.deleteFilm(film);

		Film retrievedClient = sellingManager.findFilmByTytul(TYTUL_1);

		assertEquals(null, retrievedClient);

		boolean brakujacyRekord = false;
		//porownanie bazy po aktualizacji z przed, jesli byly wczesniej dane
		if(iloscRekordowPrzedUsunieciem > 0){
			for(Film f : rekordyPrzedAktualizacja){
				if (sellingManager.findFilmById(f.getId()) == null) {
						brakujacyRekord = true;
						break;
				}
			}
		}

		assertEquals(false, brakujacyRekord);
	}


	@Test
	public void searchTwoElementName() {
		Rezyser rezyser1 = new Rezyser();
		rezyser1.setFirstName(NAME_1);
		rezyser1.setPin(PIN_1);

		sellingManager.addRezyser(rezyser1);

		Rezyser rezyser2 = new Rezyser();
		rezyser2.setFirstName(NAME_2);
		rezyser2.setPin(PIN_2);

		sellingManager.addRezyser(rezyser2);

		List<Rezyser> twoElementNameRezyser = sellingManager.searchTwoElementName();

		assertEquals(1, twoElementNameRezyser.size());
		assertEquals(PIN_2, twoElementNameRezyser.get(0).getPin());
	}


}
