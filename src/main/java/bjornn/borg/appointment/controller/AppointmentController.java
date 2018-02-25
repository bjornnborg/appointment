package bjornn.borg.appointment.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@GetMapping
	public ResponseEntity<List<TimeSlot>> getAvailableDates() {
		return ResponseEntity.ok(appointmentService.findDaysToSchedule());
	}
	
	@PostMapping
	public ResponseEntity<AppointmentRepresentation> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		Appointment appointment = this.appointmentService.createAppointment(appointmentRequest);
		
		URI uri = MvcUriComponentsBuilder
				.fromController(this.getClass())
				.path("/{id}")
				.buildAndExpand(appointment.getId())
				.toUri();
		return ResponseEntity.created(uri).body(new AppointmentRepresentation(appointment));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentRepresentation> getAppointment(@PathVariable Long id) {
		return null;
	}

}
