package bjornn.borg.appointment.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TimeSlot implements Comparable<TimeSlot> {
	
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

	@Override
	public int compareTo(TimeSlot other) {
		int order = this.start.compareTo(other.start); 
		if (order == 0) {
			order = this.end.compareTo(other.end);
		}
		
		return order;
		
	}
	
}
