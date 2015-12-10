package com.example.shdemo.service;

import java.util.List;

import com.example.shdemo.domain.Film;
import com.example.shdemo.domain.Rezyser;

public interface SellingManager {
	
	void addRezyser(Rezyser rezyser);//ok
	List<Rezyser> getAllRezyzser();
	void deleteRezyser(Rezyser rezyser);
	Rezyser findRezyserByPin(String pin);
	
	Long addFilm(Film film);
	List<Film> getAvailableFilm();
	void disposeFilm(Rezyser rezyser, Film film);


	Rezyser findRezyserById(Long id);

	List<Film> getOwnedCars(Rezyser rezyser);
	void sellCar(Long personId, Long carId);




	List<Film> getAllFilm();
	Film findFilmByTytul(String tytul);
	Film findFilmById(Long id);

	void updateFilm(Film film);
	void updateRezyser(Rezyser rezyser);

	void deleteFilm(Film film);

	List<Rezyser> searchTwoElementName();

}

//add ok testy ok
//read ok
//update ok testy ok
//delete ok testy ok