package bjornn.borg.appointment.service;

import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bjornn.borg.appointment.dao.AppointmentRepository;
import bjornn.borg.appointment.dao.CustomerRepository;
import bjornn.borg.appointment.dao.StylistTimeSlotRepository;
import bjornn.borg.appointment.exception.MandatoryDataMissingException;
import bjornn.borg.appointment.exception.UnavailableTimeSlotException;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.Customer;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private StylistTimeSlotRepository stylistTimeSlotRepository;
	
	@Autowired
	private AuditingHandler auditingHandler;
	
	@Override
	public SortedSet<TimeSlot>findDaysToSchedule() {
		List<StylistTimeSlot> availableSlots = stylistTimeSlotRepository.findAvailableSlots();
		return availableSlots.stream().map(stl -> stl.getTimeSlot()).collect(Collectors.toCollection(TreeSet::new));
	}
	
	@Override
	@Transactional
	public Appointment createAppointment(AppointmentRequest appointmentRequest) throws MandatoryDataMissingException, UnavailableTimeSlotException {
		Customer customer = customerRepository.findOne(appointmentRequest.getCustomer().getId());
		if (customer == null) {
			throw new MandatoryDataMissingException("Customer doesn't exists");
		}		
		
		TimeSlot desiredTimeSlot = appointmentRequest.getTimeSlot();		
		List<StylistTimeSlot> availableStylistsSlots = stylistTimeSlotRepository.findAvailableStylistsSlots(desiredTimeSlot);

		if (availableStylistsSlots.isEmpty()) {
			throw new UnavailableTimeSlotException("Unavailable time slot");
		}
		
		StylistTimeSlot luckyTimeSlot = availableStylistsSlots.get(new Random().nextInt(availableStylistsSlots.size()));
		Appointment appointment = appointmentRepository.save(new Appointment(luckyTimeSlot, customer));
		try { 
			this.ensureThatTimeslotIsStillMine(luckyTimeSlot);
		} catch (ObjectOptimisticLockingFailureException alreadyTaken) {
			throw new UnavailableTimeSlotException("Unavailable time slot");
		}
		return appointment;
	}
	
	private void ensureThatTimeslotIsStillMine(StylistTimeSlot stylistTimeSlot) {
		stylistTimeSlot.setTouchValue(new Random().nextInt()); // change some unimportant column to force update and version increase
		//this.auditingHandler.markModified(stylistTimeSlot); // didn't work
		stylistTimeSlotRepository.saveAndFlush(stylistTimeSlot);		
	}
	
	@Transactional
	@Override
	public Appointment reschedule(AppointmentRequest appointmentRequest) throws MandatoryDataMissingException, UnavailableTimeSlotException {
		boolean success = this.unschedule(appointmentRequest.getId());
		if (!success) {
			throw new MandatoryDataMissingException("Appointment doesn't exists");
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
