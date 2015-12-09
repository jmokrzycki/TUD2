package com.example.shdemo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.shdemo.domain.Rezyser;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Film;

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
	public void addRezyser(Rezyser rezyser) {
		rezyser.setId(null);
		sessionFactory.getCurrentSession().persist(rezyser);
	}
	
	@Override
	public void deleteRezyser(Rezyser rezyser) {
		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		
		// lazy loading here
		for (Film film : rezyser.getFilms()) {
			film.setSold(false);
			sessionFactory.getCurrentSession().update(film);
		}
		sessionFactory.getCurrentSession().delete(rezyser);
	}

	@Override
	public List<Film> getOwnedCars(Rezyser rezyser) {
		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		// lazy loading here - try this code without (shallow) copying
		List<Film> films = new ArrayList<Film>(rezyser.getFilms());
		return films;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Rezyser> getAllRezyzser() {
		return sessionFactory.getCurrentSession().getNamedQuery("person.all")
				.list();
	}

	@Override
	public Rezyser findRezyserByPin(String pin) {
		return (Rezyser) sessionFactory.getCurrentSession().getNamedQuery("person.byPin").setString("pin", pin).uniqueResult();
	}


	@Override
	public Long addFilm(Film film) {
		film.setId(null);
		return (Long) sessionFactory.getCurrentSession().save(film);
	}

	@Override
	public void sellCar(Long personId, Long carId) {
		Rezyser rezyser = (Rezyser) sessionFactory.getCurrentSession().get(
				Rezyser.class, personId);
		Film film = (Film) sessionFactory.getCurrentSession()
				.get(Film.class, carId);
		film.setSold(true);
		rezyser.getFilms().add(film);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Film> getAvailableFilm() {
		return sessionFactory.getCurrentSession().getNamedQuery("car.unsold")
				.list();
	}
	@Override
	public void disposeFilm(Rezyser rezyser, Film film) {

		rezyser = (Rezyser) sessionFactory.getCurrentSession().get(Rezyser.class,
				rezyser.getId());
		film = (Film) sessionFactory.getCurrentSession().get(Film.class,
				film.getId());

		Film toRemove = null;
		// lazy loading here (rezyser.getCars)
		for (Film aFilm : rezyser.getFilms())
			if (aFilm.getId().compareTo(film.getId()) == 0) {
				toRemove = aFilm;
				break;
			}

		if (toRemove != null)
			rezyser.getFilms().remove(toRemove);

		film.setSold(false);
	}

	@Override
	public Film findRezyserById(Long id) {
		return (Film) sessionFactory.getCurrentSession().get(Film.class, id);
	}

	@Override
	public List<Film> getAllFilm(){
		return sessionFactory.getCurrentSession().getNamedQuery("film.all").list();
	}


	@Override
	public Film findFilmByTytul(String tytul) {
		return (Film) sessionFactory.getCurrentSession().getNamedQuery("film.byTytul").setString("tytul", tytul).uniqueResult();
	}

	@Override
	public Film findFilmById(int id){
		return (Film) sessionFactory.getCurrentSession().get(Film.class, id);
	}

	@Override
	public void updateFilm(Film film){
		sessionFactory.getCurrentSession().update(film);
	}

	@Override
	public void updateRezyser(Rezyser rezyser){
		sessionFactory.getCurrentSession().update(rezyser);
	}

	@Override
	public void deleteFilm(Film film) {
		//film = (Film) sessionFactory.getCurrentSession().get(Rezyser.class,
		//		film.getId());
		//// lazy loading here
		//for (Rezyser rezyser : rezyser.getFilms()) {
		//	rezyser.setSold(false);
		//	sessionFactory.getCurrentSession().update(film);
		//}
		sessionFactory.getCurrentSession().delete(film);
	}

}
