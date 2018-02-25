package bjornn.borg.appointment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
		TimeSlot desiredTimeSlot = appointmentRequest.getTimeSlot();
		
		List<StylistTimeSlot> availableStylistsSlots = stylistTimeSlotRepository.findAvailableStylistsSlots(desiredTimeSlot);
		if (availableStylistsSlots.isEmpty()) {
			throw new RuntimeException("Unavailable time slot");
		}
		
		StylistTimeSlot luckyTimeSlot = availableStylistsSlots.get(new Random().nextInt(availableStylistsSlots.size()));
		Customer customer = customerRepository.findOne(appointmentRequest.getCustomer().getId());
		
		if (customer == null) {
			throw new RuntimeException("Customer unavailable");
		}		
		
		return appointmentRepository.save(new Appointment(luckyTimeSlot, customer));
	}
	
}
