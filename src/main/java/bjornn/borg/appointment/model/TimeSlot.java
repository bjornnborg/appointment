package bjornn.borg.appointment.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public static TimeSlot from(String start, String end) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		TimeSlot instance = new TimeSlot();
		try {
			instance.setStart(dateFormat.parse(start));
			instance.setEnd(dateFormat.parse(end));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
}
