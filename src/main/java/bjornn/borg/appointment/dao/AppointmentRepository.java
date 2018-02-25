package bjornn.borg.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bjornn.borg.appointment.model.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>, QueryAppointmentRespository {

}
