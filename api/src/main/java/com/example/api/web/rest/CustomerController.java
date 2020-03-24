package com.example.api.web.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.api.domain.Customer;
import com.example.api.domain.Endereco;
import com.example.api.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@GetMapping
	public List<Customer> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Customer findById(@PathVariable Long id) {
		return service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
	}
	
	

	
	
	@PostMapping
	public ResponseEntity<Customer> incluir(@RequestBody Customer customer, HttpServletResponse response){
		
		Endereco endereco = this.service.consultarCep(customer.getEndereco().getCep().replace("-", "").replace(".", ""));
		
		
		customer.setEndereco(new Endereco());
		customer.getEndereco().setBairro(endereco.getBairro());
		customer.getEndereco().setCep(endereco.getCep());
		customer.getEndereco().setComplemento(endereco.getComplemento());
		customer.getEndereco().setGia(endereco.getGia());
		customer.getEndereco().setLocalidade(endereco.getLocalidade());
		customer.getEndereco().setLogradouro(endereco.getLogradouro());
		customer.getEndereco().setUf(endereco.getUf());
		customer.getEndereco().setUnidade(endereco.getUnidade());
		
		Customer clienteSalvo = this.service.adicionar(customer, response);
		
		return ResponseEntity.ok(clienteSalvo);
	}
	
	@DeleteMapping("/{id}")
	public void deletarCliente(@PathVariable Long id) {

			findById(id);
			this.service.deletar(id);
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<Optional<Customer>> atualizarCliente(@Valid @RequestBody Customer customer, @PathVariable Long id, HttpServletResponse response ) {
		
		
		Endereco endereco = this.service.consultarCep(customer.getEndereco().getCep().replace("-", "").replace(".", ""));

		Optional<Customer> findCustomer = this.service.findById(id);
		
		customer.setEndereco(new Endereco());
		customer.getEndereco().setBairro(endereco.getBairro());
		customer.getEndereco().setCep(endereco.getCep());
		customer.getEndereco().setComplemento(endereco.getComplemento());
		customer.getEndereco().setGia(endereco.getGia());
		customer.getEndereco().setLocalidade(endereco.getLocalidade());
		customer.getEndereco().setLogradouro(endereco.getLogradouro());
		customer.getEndereco().setUf(endereco.getUf());
		customer.getEndereco().setUnidade(endereco.getUnidade());
		
		BeanUtils.copyProperties(customer, findCustomer.get(), "id");
		
		this.service.adicionar(findCustomer.get(), response);
		
		return ResponseEntity.ok(findCustomer);
		
	}
	

}
