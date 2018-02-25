package bjornn.borg.appointment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bjornn.borg.appointment.dao.AppointmentRepository;
import bjornn.borg.appointment.dao.CustomerRepository;
import bjornn.borg.appointment.dao.StylistTimeSlotRepository;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.Customer;
import bjornn.borg.appointment.model.entity.Stylist;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private StylistTimeSlotRepository stylistTimeSlotRepository;
	
	@Override
	public List<TimeSlot>findDaysToSchedule() {
		List<StylistTimeSlot> availableSlots = stylistTimeSlotRepository.findAvailableSlots();
		ArrayList<TimeSlot> uniqueAvailableSlots = new ArrayList<TimeSlot>(availableSlots.stream().map(stl -> stl.getTimeSlot()).collect(Collectors.toSet()));
		return uniqueAvailableSlots;
	}
	
	public boolean scheduleFor(Date when) {
		return false;
	}

	@Override
	public Appointment createAppointment(AppointmentRequest appointmentRequest) {
		Customer customer = customerRepository.findOne(appointmentRequest.getCustomer().getId());		
		if (customer == null) {
			throw new RuntimeException("Customer unavailable");
		}		
		
		TimeSlot desiredTimeSlot = appointmentRequest.getTimeSlot();		
		List<StylistTimeSlot> availableStylistsSlots = stylistTimeSlotRepository.findAvailableStylistsSlots(desiredTimeSlot);

		if (availableStylistsSlots.isEmpty()) {
			throw new RuntimeException("Unavailable time slot");
		}
		
		StylistTimeSlot luckyTimeSlot = availableStylistsSlots.get(new Random().nextInt(availableStylistsSlots.size()));
		return appointmentRepository.save(new Appointment(luckyTimeSlot, customer));
	}
	
	@Transactional
	@Override
	public Appointment reschedule(AppointmentRequest appointmentRequest) {
		boolean success = this.unschedule(appointmentRequest.getId());
		if (!success) {
			throw new RuntimeException("Appointment doesn't exist");
		}
		return this.createAppointment(appointmentRequest);
	}
	
	@Override
	public Appointment find(Long id) {
		return this.appointmentRepository.findWithAssociations(id);
	}
	
	@Override
	public List<Appointment> fromCustomer(Long customerId) {
		return this.appointmentRepository.allByCustomer(customerId);
	}
	
	@Override
	public boolean unschedule(Long id) {
		boolean result = false;
		
		Appointment appointment = this.appointmentRepository.findOne(id);
		if (appointment != null) {
			this.appointmentRepository.delete(appointment);
			result = true;
		}
		
		return result;
	}
	
}
