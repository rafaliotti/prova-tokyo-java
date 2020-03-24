package com.example.api.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.api.domain.Customer;
import com.example.api.domain.Endereco;
import com.example.api.repository.CustomerRepository;

@Service
public class CustomerService {

	private CustomerRepository repository;

	@Autowired
	public CustomerService(CustomerRepository repository) {
		this.repository = repository;
	}

	public List<Customer> findAll() {
		return repository.findAllByOrderByNameAsc();
	}

	public Optional<Customer> findById(Long id) {
		return repository.findById(id);
	}
	
	public Customer adicionar(Customer customer, HttpServletResponse response) {
		
		Customer clienteSalvo = this.repository.save(customer);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(clienteSalvo.getId()).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		return clienteSalvo;
	}

	public Endereco consultarCep(String cep) {
		RestTemplate consultarCep = new RestTemplate();
		
		return consultarCep.getForObject("https://viacep.com.br/ws/" + cep + "/json/", Endereco.class);
	}

	public void deletar(Long id) {
		
		this.repository.deleteById(id);
		
	}	
	
}
