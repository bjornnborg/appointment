package bjornn.borg.appointment.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mockito.runners.MockitoJUnitRunner;

import bjornn.borg.appointment.dao.AppointmentRepository;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@InjectMocks
	private AppointmentServiceImpl appointmentService;

	@Test
	public void shouldSortDatesToSchedule() {
		given(this.appointmentRepository.findDaysToSchedule(any(), any())).willReturn(parseDates("04/01/2018 17:00", "02/01/2018 13:00", "01/01/2018 11:00"));
		List<Date> daysToSchedule = appointmentService.findDaysToSchedule();
		
		List<String> dates = daysToSchedule.stream().map(date -> this.format(date)).collect(Collectors.toList());
		
		assertThat(dates, contains("01/01/2018 11:00", "02/01/2018 13:00", "04/01/2018 17:00"));
	}
	
	@Test
	public void shouldNotListDuplicates() {
		given(this.appointmentRepository.findDaysToSchedule(any(), any())).willReturn(parseDates("04/01/2018 17:00", "02/01/2018 13:00", "04/01/2018 17:00"));
		List<Date> daysToSchedule = appointmentService.findDaysToSchedule();
		
		List<String> dates = daysToSchedule.stream().map(date -> this.format(date)).collect(Collectors.toList());
		
		assertThat(dates, contains("02/01/2018 13:00", "04/01/2018 17:00"));
	}	
	
	private List<Date> parseDates(String... datesToParse) {
		List<Date> dates = new ArrayList<Date>();
		SimpleDateFormat format = getFormatter();
		for (String date: datesToParse) {
			try {
				dates.add(format.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dates;
	}
	
	private SimpleDateFormat getFormatter() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm");		
	}
	
	private String format(Date date) {
		return this.getFormatter().format(date);
	}
}
