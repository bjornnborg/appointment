package bjornn.borg.appointment.exception;

public class MandatoryDataMissingException extends RuntimeException {
	
	private static final long serialVersionUID = -268705607878768603L;

	public MandatoryDataMissingException(String message) {
		super(message);
	}	

}
