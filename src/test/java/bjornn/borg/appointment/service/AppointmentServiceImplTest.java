package bjornn.borg.appointment.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import bjornn.borg.appointment.dao.AppointmentRepository;
import bjornn.borg.appointment.dao.CustomerRepository;
import bjornn.borg.appointment.dao.StylistTimeSlotRepository;
import bjornn.borg.appointment.exception.MandatoryDataMissingException;
import bjornn.borg.appointment.exception.UnavailableTimeSlotException;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.model.entity.Customer;
import bjornn.borg.appointment.model.entity.Stylist;
import bjornn.borg.appointment.model.entity.StylistTimeSlot;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {

	@Mock
	private AppointmentRepository appointmentRepository;
	
	@Mock
	private CustomerRepository customerRepository;
	
	@Mock
	private StylistTimeSlotRepository stylistTimeSlotRepository;

	@InjectMocks
	private AppointmentServiceImpl appointmentService;
	
	@Test
	public void shouldCreateAppointment() {
		Customer customer = new Customer(1L, "Dummy");
		TimeSlot timeSlot = TimeSlot.from("2018-03-01 11:00", "2018-03-01 11:00");
		AppointmentRequest appointmentRequest = AppointmentRequest.builder().customer(customer).timeSlot(timeSlot).build();		
		
		given(customerRepository.findOne(1L)).willReturn(customer);
		given(stylistTimeSlotRepository.findAvailableStylistsSlots(timeSlot))
				.willReturn(Arrays.asList(getStylistTimeSlot(timeSlot)));
		
		Appointment savedAppoitment = Appointment.builder()
				.customer(customer)
				.timeSlot(getStylistTimeSlot(timeSlot))
				.id(1L)
				.build();
		given(appointmentRepository.save(any(Appointment.class))).willReturn(savedAppoitment);		
		given(stylistTimeSlotRepository.saveAndFlush(any())).willReturn(getStylistTimeSlot(timeSlot));
		
		Appointment expectedAppointment = Appointment.builder()
				.customer(customer)
				.timeSlot(getStylistTimeSlot(timeSlot))
				.id(1L)
				.build();		

		Appointment createdAppointment = this.appointmentService.createAppointment(appointmentRequest);
		
		Assert.assertEquals("Expected appointment differs from the created one", expectedAppointment, createdAppointment);
	}
	
	@Test(expected=MandatoryDataMissingException.class)
	public void shouldValidateCustomerExistence() {
		Customer customer = new Customer(1L, "Dummy");
		TimeSlot timeSlot = TimeSlot.from("2018-03-01 11:00", "2018-03-01 11:00");
		AppointmentRequest appointmentRequest = AppointmentRequest.builder().customer(customer).timeSlot(timeSlot).build();		
		
		given(customerRepository.findOne(1L)).willReturn(null);
		this.appointmentService.createAppointment(appointmentRequest);		
	}
	
	@Test(expected=UnavailableTimeSlotException.class)
	public void shouldValidateTimeSlotAvailability() {
		Customer customer = new Customer(1L, "Dummy");
		TimeSlot timeSlot = TimeSlot.from("2018-03-01 11:00", "2018-03-01 11:00");
		AppointmentRequest appointmentRequest = AppointmentRequest.builder().customer(customer).timeSlot(timeSlot).build();		
		
		given(customerRepository.findOne(1L)).willReturn(customer);
		given(stylistTimeSlotRepository.findAvailableStylistsSlots(timeSlot))
				.willReturn(new ArrayList<StylistTimeSlot>());
		
		this.appointmentService.createAppointment(appointmentRequest);		
	}
	
	@Test(expected=UnavailableTimeSlotException.class)
	public void shouldValidateTimeSlotAvailabilityBeforeCommit() {
		Customer customer = new Customer(1L, "Dummy");
		TimeSlot timeSlot = TimeSlot.from("2018-03-01 11:00", "2018-03-01 11:00");
		AppointmentRequest appointmentRequest = AppointmentRequest.builder().customer(customer).timeSlot(timeSlot).build();		
		
		given(customerRepository.findOne(1L)).willReturn(customer);
		given(stylistTimeSlotRepository.findAvailableStylistsSlots(timeSlot))
				.willReturn(Arrays.asList(getStylistTimeSlot(timeSlot)));
		
		Appointment savedAppoitment = Appointment.builder()
				.customer(customer)
				.timeSlot(getStylistTimeSlot(timeSlot))
				.id(1L)
				.build();
		given(appointmentRepository.save(any(Appointment.class))).willReturn(savedAppoitment);		
		given(stylistTimeSlotRepository.saveAndFlush(any())).willThrow(ObjectOptimisticLockingFailureException.class);
		
		this.appointmentService.createAppointment(appointmentRequest);
	}
	
	private static StylistTimeSlot getStylistTimeSlot(Long id, Stylist stylist, TimeSlot timeSlot, boolean valid) {
		return StylistTimeSlot.builder().id(id).stylist(stylist).timeSlot(timeSlot).valid(valid).build();
	}
	
	private static StylistTimeSlot getStylistTimeSlot(TimeSlot timeSlot) {
		return getStylistTimeSlot(1L, getStylist(), timeSlot, true);
	}	
	
	private static Stylist getStylist(Long id, String name) {
		return Stylist.builder().id(id).name(name).build();
	}
	
	private static Stylist getStylist() {
		return getStylist(1L, "Stylist One");
	}	

}
