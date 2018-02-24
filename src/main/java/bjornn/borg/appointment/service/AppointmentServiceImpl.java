package bjornn.borg.appointment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bjornn.borg.appointment.dao.AppointmentRepository;
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
		System.out.println(appointmentRequest);
		
		Random random = new Random(1000);
		Appointment appointment = new Appointment();
		appointment.setId(random.nextLong());
		
		Stylist stylist = new Stylist(random.nextLong(), "Some Stylist", "ready");
		TimeSlot timeSlot = TimeSlot.from("2018-08-17 11:00", "2018-08-17 11:30");
		StylistTimeSlot stylistTimeSlot = new StylistTimeSlot(random.nextLong(), stylist, timeSlot, true);
		
		appointment.setTimeSlot(stylistTimeSlot);
		appointment.setCustomer(new Customer(random.nextLong(), "Some customer"));
		return appointment;
	}
	
}
