package bjornn.borg.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import bjornn.borg.appointment.model.entity.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
	
	private Customer customer;
	private TimeSlot timeSlot;

}
