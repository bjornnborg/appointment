package bjornn.borg.appointment.model.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import bjornn.borg.appointment.model.TimeSlot;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StylistTimeSlot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Stylist stylist;
	@Embedded
	private TimeSlot timeSlot;
	private boolean valid;

}
