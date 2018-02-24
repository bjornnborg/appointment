package bjornn.borg.appointment.service;

import java.util.List;

import bjornn.borg.appointment.model.entity.TimeSlot;

public interface AppointmentService {

	List<TimeSlot> findDaysToSchedule();

}
