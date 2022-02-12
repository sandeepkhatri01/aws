package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.repository.CustomerRepository;
import com.example.repository.domain.Customer;

@RestController
@RequestMapping("/CustMgmt")
public class CustomerController {

	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping(path = "/Customer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> create(@RequestBody Customer newCustomer) throws Exception {
		return new ResponseEntity<>(customerRepository.save(newCustomer), HttpStatus.OK);
	}

	@GetMapping("Customer/{id}")
	public ResponseEntity<Customer> getById(@PathVariable long id) throws Exception {
		Optional<Customer> user = customerRepository.findById(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		} else {
			throw new Exception("Record new found");
		}
	}

}
