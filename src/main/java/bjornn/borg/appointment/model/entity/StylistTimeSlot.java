package bjornn.borg.appointment.model.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import bjornn.borg.appointment.model.TimeSlot;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"version"})
public class StylistTimeSlot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Stylist stylist;
	@Embedded
	private TimeSlot timeSlot;
	private boolean valid;
	
	@Version
	private Integer version;
	
	/*
	 * It's here so we can change only this field
	 * and force hiberante to save and increase row version.
	 * Therefore, we would be able to detect if someone else
	 * took this slot to another appointment in the meantime
	 * we were creating ours.
	 */
	private Integer touchValue; 

}
