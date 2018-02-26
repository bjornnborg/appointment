package bjornn.borg.appointment.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
/**
 * A customer booked to talk to a stylist at a given time slot
 */
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
