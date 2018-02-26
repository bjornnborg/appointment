package bjornn.borg.appointment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import bjornn.borg.appointment.model.entity.Customer;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentRequest {
	
	private Long id;
	private Customer customer;
	private TimeSlot timeSlot;

}
