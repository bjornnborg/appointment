package bjornn.borg.appointment.service;

import java.util.List;

import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;

public interface AppointmentService {

	List<TimeSlot> findDaysToSchedule();

	Appointment createAppointment(AppointmentRequest appointment);

}
