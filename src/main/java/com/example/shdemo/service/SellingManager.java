package com.example.shdemo.service;

import java.util.List;

import com.example.shdemo.domain.Car;
import com.example.shdemo.domain.Rezyser;

public interface SellingManager {
	
	void addClient(Rezyser rezyser);
	List<Rezyser> getAllClients();
	void deleteClient(Rezyser rezyser);
	Rezyser findClientByPin(String pin);
	
	Long addNewCar(Car car);
	List<Car> getAvailableCars();
	void disposeCar(Rezyser rezyser, Car car);
	Car findCarById(Long id);

	List<Car> getOwnedCars(Rezyser rezyser);
	void sellCar(Long personId, Long carId);

}
