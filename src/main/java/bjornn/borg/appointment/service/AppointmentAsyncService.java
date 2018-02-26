package bjornn.borg.appointment.service;

import java.util.List;

import bjornn.borg.appointment.model.AppointmentRequest;

public interface AppointmentAsyncService {

	void createMultiple(List<AppointmentRequest> appointmentRequests);

}
