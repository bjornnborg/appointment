package bjornn.borg.appointment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import bjornn.borg.appointment.dao.StylistTimeSlotRepository;
import bjornn.borg.appointment.exception.MandatoryDataMissingException;
import bjornn.borg.appointment.exception.UnavailableTimeSlotException;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

@Service
public class AppointmentAsyncServiceImpl implements AppointmentAsyncService {
	
	@Autowired
	private StylistTimeSlotRepository stylistTimeSlotRepository;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Async
	@Override
	public void createMultiple(List<AppointmentRequest> appointmentRequests) {
		Map<Long, Appointment> appointments = new HashMap<Long, Appointment>();
		List<StylistTimeSlot> availableStylistsSlots = new ArrayList<StylistTimeSlot>();
		
		for (Iterator<AppointmentRequest> requestsIterator = appointmentRequests.iterator(); requestsIterator.hasNext(); ) {
			AppointmentRequest appointmentRequest = requestsIterator.next();
			
			if (availableStylistsSlots.isEmpty()) { 
				availableStylistsSlots = stylistTimeSlotRepository.findAvailableSlots(); // eager load associations. Limit?
			}
			
			slotSearch: 
			while (!availableStylistsSlots.isEmpty()) {
				boolean appointmentSuccess = false;
				
				for (Iterator<StylistTimeSlot> iterator = availableStylistsSlots.iterator(); iterator.hasNext(); ) {
					appointmentRequest.setTimeSlot(iterator.next().getTimeSlot());
					try {
						Appointment appointment = appointmentService.createAppointment(appointmentRequest);
						appointments.put(appointmentRequest.getCustomer().getId(), appointment);
						iterator.remove();
						appointmentSuccess = true;
						break; // we are done with this customer
					} catch (MandatoryDataMissingException e) {
						break slotSearch; // no way to proceed
					} catch (UnavailableTimeSlotException unavailable) {
						iterator.remove(); // ok to keep searching for another time slot
					}
				}
				
				if (appointmentSuccess) {
					break;
				} else {
					availableStylistsSlots = stylistTimeSlotRepository.findAvailableSlots(); // eager load associations. Limit?
				}
			}
		}

	}	

}
