package bjornn.borg.appointment.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import bjornn.borg.appointment.dao.QueryAppointmentRespository;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.Customer;

public class AppointmentRepositoryImpl implements QueryAppointmentRespository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Appointment findWithAssociations(Long id) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
		Root<Appointment> appointment = criteria.from(Appointment.class);
		appointment.fetch("timeSlot").fetch("stylist");
		appointment.fetch("customer");
		
		criteria.where(
			builder.equal(appointment.get("id"), id)
		);
		
		criteria.select(appointment);
		
		TypedQuery<Appointment> query = entityManager.createQuery(criteria);
		List<Appointment> result = query.getResultList();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	public List<Appointment> allByCustomer(Long customerId) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Appointment> criteria = builder.createQuery(Appointment.class);
		Root<Appointment> appointment = criteria.from(Appointment.class);
		appointment.fetch("timeSlot").fetch("stylist");
		//Fetch<Appointment, Customer> customerJoin = appointment.fetch("customer");
		Join<Appointment, Customer> customerJoin = appointment.join("customer");
		
		criteria.where(
			builder.equal(customerJoin.get("id"), customerId)
		);
		
		criteria.select(appointment);
		
		TypedQuery<Appointment> query = entityManager.createQuery(criteria);
		return query.getResultList();
	}

}
