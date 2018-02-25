package bjornn.borg.appointment.service;

import java.util.List;

import bjornn.borg.appointment.exception.MandatoryDataMissingException;
import bjornn.borg.appointment.exception.UnavailableTimeSlotException;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;

public interface AppointmentService {
	List<TimeSlot> findDaysToSchedule();
	Appointment createAppointment(AppointmentRequest appointment) throws MandatoryDataMissingException, UnavailableTimeSlotException;
	Appointment find(Long id);
	List<Appointment> fromCustomer(Long customerId);
	boolean unschedule(Long id);
	Appointment reschedule(AppointmentRequest appointmentRequest) throws MandatoryDataMissingException, UnavailableTimeSlotException;

}
