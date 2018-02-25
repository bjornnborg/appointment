package bjornn.borg.appointment.dao;

import java.util.List;

import bjornn.borg.appointment.model.entity.Appointment;


public interface QueryAppointmentRespository {
	
	public Appointment findWithAssociations(Long id);
	public List<Appointment> allByCustomer(Long customerId);
	
}
