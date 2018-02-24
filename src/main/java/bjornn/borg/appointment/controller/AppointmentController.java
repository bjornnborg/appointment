package bjornn.borg.appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjornn.borg.appointment.model.entity.TimeSlot;
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

}
