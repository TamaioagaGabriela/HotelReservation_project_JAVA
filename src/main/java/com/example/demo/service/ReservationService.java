package com.example.demo.service;

import com.example.demo.dto.ReservationDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ClientException;
import com.example.demo.exception.ReservationException;
import com.example.demo.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private HotelRepository hotelRepository;


    public ReservationService(ReservationRepository reservationRepository, RoomRepository roomRepository, ClientRepository clientRepository, HotelRepository hotelRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
        this.clientRepository = clientRepository;
        this.hotelRepository = hotelRepository;
    }

    public ReservationDto entityToDto(ReservationEntity reservationEntity){
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setIdReservation(reservationEntity.getIdReservation());
        reservationDto.setStartDate(reservationEntity.getStartDate());
        reservationDto.setEndDate(reservationEntity.getEndDate());
        reservationDto.setPeriod(reservationEntity.getPeriod());
        reservationDto.setIdClientReservation(reservationEntity.getClientReservation().getIdClient());
        reservationDto.setIdRoomReservation(reservationEntity.getRoomReservation().getIdRoom());
        reservationDto.setClientLastName(reservationEntity.getClientReservation().getLastName());
        reservationDto.setClientFirstName(reservationEntity.getClientReservation().getFirstName());
        reservationDto.setRoomNumber(reservationEntity.getRoomReservation().getNumberRoom());
        reservationDto.setRoomType(reservationEntity.getRoomReservation().getRoomType());
        reservationDto.setCost(reservationEntity.getRoomReservation().getCost());
        reservationDto.setCostTotal(reservationDto.getCost()*reservationDto.getPeriod());
        reservationDto.setHotelCity(reservationEntity.getRoomReservation().getHotelRoom().getCity());
        reservationDto.setHotelAddress(reservationEntity.getRoomReservation().getHotelRoom().getAddress());

        return reservationDto;
    }

    public ReservationEntity dtoToEntity(ReservationDto reservationDto){
        ReservationEntity reservationEntity = new ReservationEntity();

        reservationEntity.setStartDate(reservationDto.getStartDate());
        reservationEntity.setEndDate(reservationDto.getEndDate());
        reservationEntity.setPeriod(reservationDto.getPeriod());
        reservationEntity.setClientReservation(clientRepository.findById(reservationDto.getIdClientReservation()).orElse(null));
        reservationEntity.setRoomReservation(roomRepository.findById(reservationDto.getIdRoomReservation()).orElse(null));

        return  reservationEntity;
    }

    public int collideReservations(ReservationEntity res1, ReservationDto res2)
    {
        if (res1.getEndDate().isBefore(res2.getStartDate()) || res1.getStartDate().isAfter(res2.getEndDate()))
            return 1; // nu se intersecteaza
        else return 0; // se intersecteaza
    }

    // ADD RESERVATION => POST
    public ReservationEntity addReservation(ReservationDto reservation){
        log.info("reservation for add", reservation);
        if(reservation.getStartDate().getHour() < 14 && reservation.getEndDate().getHour() > 12 )
        {
            throw ReservationException.invalidCheckInCheckOut();
        }
        RoomEntity roomEntity = new RoomEntity();
        HotelEntity hotelEntity = hotelRepository.findAllByCityAndAddress(reservation.getHotelCity(), reservation.getHotelAddress());

        List<RoomEntity> roomEntities = roomRepository.findAllByRoomTypeAndHotelRoom(reservation.getRoomType(), hotelEntity);
        // lista cu toate camerele de un anumit tip de camera si dupa id-ul hotelului
        int ok;
        for(int i = 0; i < roomEntities.size(); i++)
        {
            // o lista cu rezervarile dintr-o camera de un anumit tip(SINGLE, DOUBLE, SUITE)
            List<ReservationEntity> reservationEntities = roomEntities.get(i).getReservationsRoom();
            log.info(String.valueOf(reservationEntities.size()));
            ok = 1; // presupun ca nu se intersecteaza cu nicio alta rezervare
            for(int j = 0; j < reservationEntities.size(); j++)
            {
                log.info(String.valueOf(collideReservations(reservationEntities.get(j), reservation)));
               if(collideReservations(reservationEntities.get(j), reservation) == 0) {
                   log.info(String.valueOf(collideReservations(reservationEntities.get(j), reservation)));
                   ok = 0; // presupunerea pica
               }
            }
            if (ok == 1) {
                roomEntity = roomEntities.get(i);
                break;
            }
        }
        log.info(String.valueOf(roomEntity));
        if(roomEntity.getIdRoom() == 0)
        {
            throw ReservationException.reservationNotSaved();
        }
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setStartDate(reservation.getStartDate());
        reservationEntity.setEndDate(reservation.getEndDate());
        reservationEntity.setPeriod(reservation.getPeriod());
        reservationEntity.setClientReservation(clientRepository.findByIdClient(reservation.getIdClientReservation()));
        reservationEntity.setRoomReservation(roomRepository.findByIdRoom(roomEntity.getIdRoom()));
        return reservationRepository.save(reservationEntity);
    }


    //  GET ALL RESERVATIONS USING DIFFERENT PARAMS
    public List<ReservationDto> getReservations(LocalDateTime startDate, LocalDateTime endDate,Integer period){
        List<ReservationEntity>  reservationEntities = reservationRepository.findAll().stream()
                .filter( reservation -> isMatch(reservation, startDate, endDate, period))
                .collect(Collectors.toList());
        if(reservationEntities.isEmpty()){
            throw ReservationException.reservationDoesNotExist();
        }
        return reservationEntities.stream()
                .map(reservationEntity -> entityToDto(reservationEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(ReservationEntity reservation, LocalDateTime startDate, LocalDateTime endDate, Integer period) {
        return (startDate == null || reservation.getStartDate().equals(startDate)
                ) &&
                (endDate == null || reservation.getEndDate().equals(endDate)
                ) &&
                (period == null || reservation.getPeriod().equals(period)
                );
    }


    // GET A REVIEW BY ID ROOM
    public List<ReservationDto> findAllByIdRoomReservation(int id){

        RoomEntity room = roomRepository.findByIdRoom(id);

        if (room == null ) {
            throw ReservationException.reservationRoomDoesNotExist();
        }
        // preluam lista cu rezervarile rezultata dupa id-ul camerei
        List<ReservationEntity>  reservationEntities =  room.getReservationsRoom();
        // transfomare entity to dto
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                .map(reservationEntity -> entityToDto(reservationEntity))
                .collect(Collectors.toList());
        return reservationDtos;
    }

    // GET A RESERVATION BY ID CLIENT
    public List<ReservationDto> findAllByIdClientReservation(int id){
        ClientEntity client = clientRepository.findByIdClient(id);
        log.info(String.valueOf(client));
        if (client == null) {
            throw ReservationException.reservationClientDoesNotExist();
        }
        List<ReservationEntity>  reservationEntities =  client.getReservationsClient();
        log.info(String.valueOf(reservationEntities));
        if (reservationEntities.isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client does not have reservations");
        }
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                .map(reservationEntity -> entityToDto(reservationEntity))
                .collect(Collectors.toList());
        return reservationDtos;
    }

    //  GET A RESERVATION BY ID
    public ReservationDto getReservationById(int id) {

        log.info(" get reservation with id " , String.valueOf(id));
        ReservationEntity reservationEntity =  reservationRepository.findByIdReservation(id);
        if (reservationEntity == null)
        {
            throw ReservationException.reservationDoesNotExist();
        }
        ReservationDto reservationDto = entityToDto(reservationEntity);
        return reservationDto;
    }

    // mai trebuie modificat aici sa fie in functie de start date si end date
    //  UPDATE RESERVATION'S INFO => PUT
    public ReservationDto updateReservation(int id, ReservationDto reservation){
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElse(null);
        if(reservationEntity == null){
            throw ReservationException.reservationDoesNotExist();
        }
        if(reservation.getStartDate().getHour() < 14 && reservation.getEndDate().getHour() > 12 )
        {
            throw ReservationException.reservationNotSaved();
        }
        reservationEntity.setStartDate(reservation.getStartDate());
        reservationEntity.setEndDate(reservation.getEndDate());
        reservationEntity.setPeriod(reservation.getPeriod());

        return entityToDto(reservationRepository.save(reservationEntity));
    }

    //  DELETE A RESERVATION
    public void deleteReservation(int id){
        ReservationEntity reservationEntity = reservationRepository.findById(id).orElse(null);
        if(reservationEntity == null){
            throw ReservationException.reservationDoesNotExist();
        }
        reservationRepository.deleteById(id);
    }

}
