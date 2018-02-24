package bjornn.borg.appointment.dao;

import java.util.List;

import bjornn.borg.appointment.model.entity.StylistTimeSlot;

public interface QueryStylistTimeSlotRepository {

	List<StylistTimeSlot> findAvailableSlots();

}
