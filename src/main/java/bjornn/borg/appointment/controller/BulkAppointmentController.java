package bjornn.borg.appointment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjornn.borg.appointment.model.AppointmentRequest;
import bjornn.borg.appointment.service.AppointmentAsyncService;

@RestController
@RequestMapping("/api/appointment/bulk")
public class BulkAppointmentController {
	
	@Autowired
	private AppointmentAsyncService appointmentAsyncService;
	
	@PostMapping
	public ResponseEntity<?> bulkSchedule(@RequestBody List<AppointmentRequest> bulkRequests) {
		appointmentAsyncService.createMultiple(bulkRequests);
		return ResponseEntity.accepted().build();
		
	}

}
