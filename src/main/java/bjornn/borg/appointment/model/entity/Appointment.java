package bjornn.borg.appointment.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private StylistTimeSlot timeSlot;
	@OneToOne
	private Customer customer;
	
	public Appointment(StylistTimeSlot timeSlot, Customer customer) {
		this.timeSlot = timeSlot;
		this.customer = customer;
	}
	
}
