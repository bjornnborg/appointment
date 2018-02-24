package bjornn.borg.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import bjornn.borg.appointment.model.entity.StylistTimeSlot;

public interface StylistTimeSlotRepository extends JpaRepository<StylistTimeSlot, Long>, QueryStylistTimeSlotRepository {

}
