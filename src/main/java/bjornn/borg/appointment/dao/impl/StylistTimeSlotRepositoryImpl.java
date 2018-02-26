package bjornn.borg.appointment.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import bjornn.borg.appointment.dao.QueryStylistTimeSlotRepository;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.Stylist;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

public class StylistTimeSlotRepositoryImpl implements QueryStylistTimeSlotRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<StylistTimeSlot> findAvailableSlots() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StylistTimeSlot> criteria = builder.createQuery(StylistTimeSlot.class);
		Root<StylistTimeSlot> stylistSlot = criteria.from(StylistTimeSlot.class);
		Join<StylistTimeSlot, Stylist> stylistJoin = stylistSlot.join("stylist");
		
		Subquery<Long> subquery = criteria.subquery(Long.class);
		Root<Appointment> subqueryFrom = subquery.from(Appointment.class);
		subquery.select(subqueryFrom.get("timeSlot"));
		
		criteria.where(
			builder.equal(stylistSlot.get("valid"), true),
			builder.equal(stylistJoin.get("status"), "ready"),
			builder.not(stylistSlot.get("id").in(subquery))
			// limit by date range also...
		);
		
		criteria.select(stylistSlot);
		
		TypedQuery<StylistTimeSlot> query = entityManager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public List<Stylist> findAvailableStylists(TimeSlot desiredTimeSlot) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Stylist> criteria = builder.createQuery(Stylist.class);
		Root<Stylist> stylist = criteria.from(Stylist.class);
		Join<Stylist, StylistTimeSlot> stylistTimeSlotJoin = stylist.join("stylistTimeSlot");
		
		Subquery<Long> subquery = criteria.subquery(Long.class);
		Root<Appointment> subqueryFrom = subquery.from(Appointment.class);
		subquery.select(subqueryFrom.get("timeSlot").get("id"));
		
		criteria.where(
			builder.equal(stylist.get("status"), "ready"),
			builder.equal(stylistTimeSlotJoin.get("valid"), true),
			builder.equal(stylistTimeSlotJoin.get("timeSlot").get("start"), desiredTimeSlot.getStart()),
			builder.equal(stylistTimeSlotJoin.get("timeSlot").get("end"), desiredTimeSlot.getStart()),
			//builder.not(stylistTimeSlotJoin.get("id").in(subquery))
			builder.not(builder.exists(subquery))
		);
		
		criteria.select(stylist);
		
		TypedQuery<Stylist> query = entityManager.createQuery(criteria);
		return query.getResultList();		
		
	}
	
	@Override
	public List<StylistTimeSlot> findAvailableStylistsSlots(TimeSlot desiredTimeSlot) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StylistTimeSlot> criteria = builder.createQuery(StylistTimeSlot.class);
		Root<StylistTimeSlot> stylistTimeSlot = criteria.from(StylistTimeSlot.class);
		Join<StylistTimeSlot, Stylist> stylistTimeSlotJoin = stylistTimeSlot.join("stylist");
		//stylistTimeSlot.fetch("stylist");
		
		Subquery<Long> subquery = criteria.subquery(Long.class);
		Root<Appointment> subqueryFrom = subquery.from(Appointment.class);
		subquery.select(subqueryFrom.get("timeSlot").get("id"));
		
		criteria.where(
			builder.equal(stylistTimeSlotJoin.get("status"), "ready"),
			builder.equal(stylistTimeSlot.get("valid"), true),
			builder.equal(stylistTimeSlot.get("timeSlot").get("start"), desiredTimeSlot.getStart()),
			builder.equal(stylistTimeSlot.get("timeSlot").get("end"), desiredTimeSlot.getEnd()),
			builder.not(stylistTimeSlot.get("id").in(subquery))
			//builder.not(builder.exists(subquery))
		);
		
		criteria.select(stylistTimeSlot);
		
		TypedQuery<StylistTimeSlot> query = entityManager.createQuery(criteria);
		return query.getResultList();		
		
	}	

}
