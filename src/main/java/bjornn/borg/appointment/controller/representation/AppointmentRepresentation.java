package bjornn.borg.appointment.controller.representation;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

import bjornn.borg.appointment.controller.AppointmentController;
import bjornn.borg.appointment.model.entity.Appointment;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Data
@EqualsAndHashCode(callSuper=false)
public class AppointmentRepresentation extends ResourceSupport {
	
	private Appointment appointment;
	
	public AppointmentRepresentation(Appointment model) {
		this.appointment = model;
		this.add(linkTo(methodOn(AppointmentController.class).getAppointment(appointment.getId())).withSelfRel());
	}

}
