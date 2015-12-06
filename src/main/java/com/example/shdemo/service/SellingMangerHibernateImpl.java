package com.example.shdemo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.shdemo.domain.Rezyser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Car;

@Component
@Transactional
public class SellingMangerHibernateImpl implements SellingManager {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public void addClient(Rezyser rezyser) {
		rezyser.setId(null);
		sessionFactory.getCurrentSession().persist(rezyser);
	}
	
	@Override
	public void deleteClient(Rezyser rezyser) {
		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		
		// lazy loading here
		for (Car car : rezyser.getCars()) {
			car.setSold(false);
			sessionFactory.getCurrentSession().update(car);
		}
		sessionFactory.getCurrentSession().delete(rezyser);
	}

	@Override
	public List<Car> getOwnedCars(Rezyser rezyser) {
		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		// lazy loading here - try this code without (shallow) copying
		List<Car> cars = new ArrayList<Car>(rezyser.getCars());
		return cars;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Rezyser> getAllClients() {
		return sessionFactory.getCurrentSession().getNamedQuery("person.all")
				.list();
	}

	@Override
	public Rezyser findClientByPin(String pin) {
		return (Rezyser) sessionFactory.getCurrentSession().getNamedQuery("person.byPin").setString("pin", pin).uniqueResult();
	}


	@Override
	public Long addNewCar(Car car) {
		car.setId(null);
		return (Long) sessionFactory.getCurrentSession().save(car);
	}

	@Override
	public void sellCar(Long personId, Long carId) {
		Rezyser rezyser = (Rezyser) sessionFactory.getCurrentSession().get(
				Rezyser.class, personId);
		Car car = (Car) sessionFactory.getCurrentSession()
				.get(Car.class, carId);
		car.setSold(true);
		rezyser.getCars().add(car);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Car> getAvailableCars() {
		return sessionFactory.getCurrentSession().getNamedQuery("car.unsold")
				.list();
	}
	@Override
	public void disposeCar(Rezyser rezyser, Car car) {

		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		car = (Car) sessionFactory.getCurrentSession().get(Car.class,
				car.getId());

		Car toRemove = null;
		// lazy loading here (rezyser.getCars)
		for (Car aCar : rezyser.getCars())
			if (aCar.getId().compareTo(car.getId()) == 0) {
				toRemove = aCar;
				break;
			}

		if (toRemove != null)
			rezyser.getCars().remove(toRemove);

		car.setSold(false);
	}

	@Override
	public Car findCarById(Long id) {
		return (Car) sessionFactory.getCurrentSession().get(Car.class, id);
	}

}
