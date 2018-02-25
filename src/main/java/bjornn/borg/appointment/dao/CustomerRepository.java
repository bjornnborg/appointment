package bjornn.borg.appointment.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import bjornn.borg.appointment.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
