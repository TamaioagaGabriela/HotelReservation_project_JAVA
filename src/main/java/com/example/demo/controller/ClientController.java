package com.example.demo.controller;

import com.example.demo.dto.ClientDto;
import com.example.demo.entity.ClientEntity;
import com.example.demo.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private Validator validator;

    @Autowired
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // ADD CLIENT => POST
    @PostMapping
    public ResponseEntity<ClientEntity> addClient (@Valid @RequestBody ClientDto client)
    {
        ClientEntity clientEntity = clientService.addClient(client);
        return new ResponseEntity<>(clientEntity, HttpStatus.CREATED);
    }


    //  GET ALL CLIENTS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<ClientDto>> getClients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumberClient) {

       List<ClientDto> clientDtos = clientService.getClients(lastName, firstName, email, phoneNumberClient);
        return clientDtos.isEmpty() ?
                ResponseEntity.noContent().build()
               : ResponseEntity.ok(clientDtos);
    }


    //  GET A CLIENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClient(@PathVariable int id) {
        log.info("Received request to get employee with id: {}", id);
        ClientDto clientDto = clientService.getClientById(id);
        log.info("Created returnedEmployeeEntity with id: {}", id);
        if (clientDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        log.debug("before return");
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }


    //  DELETE A CLIENT
    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable int id) {
        log.info("Received request to delete employee with id: {}", id);
        clientService.deleteClient(id);
    }


    //  UPDATE INFO'S CLIENT => PUT
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable int id, @RequestBody ClientDto client){
        ClientDto clientDto = clientService.updateClient(id, client);
        if(clientDto== null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        return new ResponseEntity<>(clientDto, HttpStatus.OK);
    }

    private void validate(Object[] objects){
        String message = Arrays.stream(objects)
                .map(object -> validator.validate(object).stream()
                        .map(contraintViolation -> contraintViolation.getMessage())
                        .filter(error -> !error.isBlank())
                        .collect(Collectors.joining("|"))).filter(error -> !error.isBlank())
                .collect(Collectors.joining("|"));
        if(!message.isBlank()){
            throw new ValidationException((message));
        }
    }

}
