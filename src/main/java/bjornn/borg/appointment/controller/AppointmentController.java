package bjornn.borg.appointment.controller;

import java.net.URI;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import bjornn.borg.appointment.controller.representation.AppointmentRepresentation;
import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.model.TimeSlot;
import bjornn.borg.appointment.model.entity.Appointment;
import bjornn.borg.appointment.service.AppointmentService;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@GetMapping("/timeslots")
	public ResponseEntity<SortedSet<TimeSlot>> getAvailableDates() {
		return ResponseEntity.ok(appointmentService.findDaysToSchedule());
	}
	
	@PostMapping
	public ResponseEntity<AppointmentRepresentation> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		Appointment appointment = this.appointmentService.createAppointment(appointmentRequest);
		
		URI uri = MvcUriComponentsBuilder.fromController(this.getClass())
				.path("/{id}").buildAndExpand(appointment.getId())
				.toUri();
		return ResponseEntity.created(uri).body(new AppointmentRepresentation(appointment));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<AppointmentRepresentation> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest appointmentRequest) {
		appointmentRequest.setId(id);
		Appointment appointment = this.appointmentService.reschedule(appointmentRequest);
		
		return appointment != null ? ResponseEntity.ok(new AppointmentRepresentation(appointment)) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}	
	
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentRepresentation> getAppointment(@PathVariable Long id) {
		Appointment appointment = this.appointmentService.find(id);
		return appointment != null ? ResponseEntity.ok(new AppointmentRepresentation(appointment)) : new ResponseEntity<>(HttpStatus.NOT_FOUND);		 
	}
	
	@GetMapping("/mine/{customerId}")
	public ResponseEntity<List<AppointmentRepresentation>> mine(@PathVariable Long customerId) {
		List<Appointment> appointments = this.appointmentService.fromCustomer(customerId);
		return ResponseEntity.ok(appointments.stream()
				.map(app -> new AppointmentRepresentation(app))
				.collect(Collectors.toList()));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		boolean success = this.appointmentService.unschedule(id);
		return success ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
