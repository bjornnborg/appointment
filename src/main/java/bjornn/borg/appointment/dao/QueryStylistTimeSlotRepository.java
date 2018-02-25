package bjornn.borg.appointment.dao;

import java.util.List;

import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Stylist;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

public interface QueryStylistTimeSlotRepository {

	List<StylistTimeSlot> findAvailableSlots();
	List<Stylist> findAvailableStylists(TimeSlot desiredTimeSlot);
	List<StylistTimeSlot> findAvailableStylistsSlots(TimeSlot desiredTimeSlot);

}
