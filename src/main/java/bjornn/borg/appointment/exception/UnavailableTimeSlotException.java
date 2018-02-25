package bjornn.borg.appointment.exception;

public class UnavailableTimeSlotException extends RuntimeException {

	private static final long serialVersionUID = -5162695857613609186L;
	
	public UnavailableTimeSlotException(String message) {
		super(message);
	}

}
