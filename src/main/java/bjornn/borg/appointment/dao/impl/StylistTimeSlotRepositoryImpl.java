package bjornn.borg.appointment.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import bjornn.borg.appointment.dao.QueryStylistTimeSlotRepository;
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

}
