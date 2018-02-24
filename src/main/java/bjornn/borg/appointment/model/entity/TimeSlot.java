package bjornn.borg.appointment.model.entity;

import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
	
	private Date start;
	private Date end;
	
}
