package com.example.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.repository.domain.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	List<Customer> findByLastName(String lastName);

	Optional<Customer> findById(long id);

	@SuppressWarnings("unchecked")
	Customer save(Customer customer);

}
