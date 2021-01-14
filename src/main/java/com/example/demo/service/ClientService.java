package com.example.demo.service;

import com.example.demo.exception.ClientException;
import com.example.demo.dto.ClientDto;
import com.example.demo.dto.PaymentDto;
import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.entity.ReservationEntity;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ReviewService reviewService;


    public ClientService(ClientRepository clientRepository, ReservationService reservationService, PaymentService paymentService, ReviewService reviewService) {
        this.clientRepository = clientRepository;
        this.reservationService = reservationService;
        this.paymentService = paymentService;
        this.reviewService = reviewService;
    }

    public ClientDto entityToDto(ClientEntity clientEntity){
        ClientDto clientDto = new ClientDto();

        clientDto.setIdClient(clientEntity.getIdClient());
        clientDto.setLastName(clientEntity.getLastName());
        clientDto.setFirstName(clientEntity.getFirstName());
        clientDto.setEmail(clientEntity.getEmail());
        clientDto.setPhoneNumberClient(clientEntity.getPhoneNumberClient());

        List<ReviewEntity> reviewEntities = clientEntity.getReviews();
        List<ReviewDto> reviewDtos = reviewEntities.stream()
                .map(reviewEntity -> reviewService.entityToDto(reviewEntity))
                .collect(Collectors.toList());
        clientDto.setReviewsClient(reviewDtos);

        List<ReservationEntity> reservationEntities = clientEntity.getReservationsClient();
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                .map(reservationEntity -> reservationService.entityToDto(reservationEntity))
                .collect(Collectors.toList());
        clientDto.setReservationsClient(reservationDtos);

        List<PaymentEntity> paymentEntities = clientEntity.getPayments();
        List<PaymentDto> paymentDtos = paymentEntities.stream()
                .map(paymentEntity -> paymentService.entityToDto(paymentEntity))
                .collect(Collectors.toList());
        clientDto.setPaymentsClient(paymentDtos);

        return clientDto;
    }


    public ClientEntity dtoToEntity(ClientDto clientDto){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setLastName(clientDto.getLastName());
        clientEntity.setFirstName(clientDto.getFirstName());
        clientEntity.setPhoneNumberClient(clientDto.getPhoneNumberClient());
        clientEntity.setEmail(clientDto.getEmail());

        return clientEntity;
    }

    // ADD CLIENT => POST
    public ClientEntity addClient(ClientDto client){
        Optional<ClientEntity> existingClient = clientRepository.findByEmail(client.getEmail());
        if (existingClient.isPresent()) {
            throw ClientException.clientWithSameEmailAlreadyExists();
        }
        if (client.getFirstName() == null || client.getLastName() == null)
        {
            throw ClientException.clientWithNoLastNameOrFirstName();
        }
        ClientEntity clientEntity = dtoToEntity(client);
        log.info("client for add", client);
        return clientRepository.save(clientEntity);
    }


    //  GET ALL  CLIENTS USING DIFFERENT PARAMS
    public List<ClientDto> getClients(String lastName, String firstName, String email, String phoneNumberClient){
        List<ClientEntity> clientEntities =  clientRepository.findAll().stream()
                .filter( client -> isMatch(client, lastName, firstName, email, phoneNumberClient))
                .collect(Collectors.toList());
        if(clientEntities.isEmpty())
        {
            throw ClientException.clientDoesNotExist();
        }
        return clientEntities.stream()
                .map(clientEntity -> entityToDto(clientEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(ClientEntity client, String lastName, String firstName, String email, String phoneNumberClient) {
        return ( lastName == null
                || client.getLastName().toLowerCase().startsWith(lastName.toLowerCase())
                ) &&
                ( firstName == null
                        || client.getFirstName().toLowerCase().startsWith(firstName.toLowerCase())
                ) &&
                ( email == null
                        || client.getEmail().toLowerCase().startsWith(email.toLowerCase())
                ) &&
                ( phoneNumberClient == null
                        || client.getPhoneNumberClient().equals(phoneNumberClient)
                );
    }

    //  GET A CLIENT BY ID
    public ClientDto getClientById(int id) {
        ClientEntity clientEntity = clientRepository.findByIdClient(id);
        if(clientEntity == null)
        {
            throw ClientException.clientDoesNotExist();
        }
        return entityToDto(clientEntity);
    }

    //  UPDATE CLIENT'S INFO => PUT
    public ClientDto updateClient(int id, ClientDto client){
        ClientEntity clientEntity = clientRepository.findById(id).orElse(null);
        if(clientEntity == null){
            throw ClientException.clientDoesNotExist();
        }
        clientEntity.setLastName(client.getLastName());
        clientEntity.setFirstName(client.getFirstName());
        clientEntity.setEmail(client.getEmail());
        clientEntity.setPhoneNumberClient(client.getPhoneNumberClient());

        return entityToDto(clientRepository.save(clientEntity));
    }

    //  DELETE A CLIENT
    public void deleteClient(int id){
        ClientEntity clientEntity = clientRepository.findById(id).orElse(null);
        if(clientEntity == null){
            throw ClientException.clientDoesNotExist();
        }
        clientRepository.deleteById(id);
    }

}
