package bjornn.borg.appointment.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bjornn.borg.appointment.dao.AppointmentRepository;
import bjornn.borg.appointment.dao.StylistTimeSlotRepository;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;
import bjornn.borg.appointment.model.entity.TimeSlot;

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
	
}
