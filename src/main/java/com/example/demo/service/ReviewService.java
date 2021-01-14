package com.example.demo.service;


import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.HotelEntity;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.exception.ReviewException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ReviewService(ReviewRepository reviewRepository, HotelRepository hotelRepository, ClientRepository clientRepository) {
        this.reviewRepository = reviewRepository;
        this.hotelRepository = hotelRepository;
        this.clientRepository = clientRepository;
    }

    public ReviewDto entityToDto(ReviewEntity reviewEntity){
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setIdReview(reviewEntity.getIdReview());
        reviewDto.setSubject(reviewEntity.getSubject());
        reviewDto.setRate(reviewEntity.getRate());
        reviewDto.setDescription(reviewEntity.getDescription());
        reviewDto.setIdClientReview(reviewEntity.getClientReview().getIdClient());
        reviewDto.setClientLastName(reviewEntity.getClientReview().getLastName());
        reviewDto.setClientFirstName(reviewEntity.getClientReview().getFirstName());
        reviewDto.setIdHotelReview(reviewEntity.getHotelReview().getIdHotel());
        reviewDto.setHotelCity(reviewEntity.getHotelReview().getCity());
        reviewDto.setHotelAddress(reviewEntity.getHotelReview().getAddress());

        return reviewDto;
    }

    public ReviewEntity dtoToEntity(ReviewDto review){
        ReviewEntity reviewEntity = new ReviewEntity();

        reviewEntity.setSubject(review.getSubject());
        reviewEntity.setDescription(review.getDescription());
        reviewEntity.setRate(review.getRate());
        reviewEntity.setClientReview(clientRepository.findById(review.getIdClientReview()).orElse(null));
        reviewEntity.setHotelReview(hotelRepository.findById(review.getIdHotelReview()).orElse(null));
        return reviewEntity;
    }

    // ADD REVIEW => POST
    public ReviewEntity addReview(ReviewDto review){
        log.info("review for add", review);
        if(review.getRate() > 5){
            throw ReviewException.rateTooHigh();
        }
        else if(review.getDescription() == null || review.getSubject() == null || review.getRate() == null) {
            throw ReviewException.fieldsNullNotSaved();
        }
        ReviewEntity reviewEntity = dtoToEntity(review);
        if( reviewEntity == null){
            throw ReviewException.reviewNotSaved();
        }
        return reviewRepository.save(reviewEntity);
    }


    //  GET ALL REVIEWS USING DIFFERENT PARAMS
    public List<ReviewDto> getReviews(String subject, String description, Integer rate){
        List<ReviewEntity> reviewEntities = reviewRepository.findAll().stream()
                .filter( review -> isMatch(review, subject, description, rate))
                .collect(Collectors.toList());
        if(reviewEntities.isEmpty())
        {
            throw ReviewException.reviewNotFound();
        }
        return reviewEntities.stream()
                .map( reviewEntity -> entityToDto(reviewEntity))
                .collect(Collectors.toList());
    }

    private boolean isMatch(ReviewEntity review, String subject, String description, Integer rate) {
        return (subject == null || review.getSubject().equals(subject)
                ) &&
                (description == null || review.getDescription().equals(description)
                ) &&
                (rate == null || review.getRate().equals(rate)
                );
    }

    // GET A REVIEW BY ID HOTEL
    public List<ReviewDto> findAllByIdHotelReview(int id){

        Optional<HotelEntity> hotel = hotelRepository.findById(id);

        if (hotel.isEmpty()) {
            throw ReviewException.hotelNotFound();
        }
        List<ReviewEntity> reviewEntities =  hotel.get().getReviews();
        if(reviewEntities.isEmpty()){
            throw ReviewException.reviewWithIdHotelNotFound();
        }

        return reviewEntities.stream()
                .map(reviewEntity -> entityToDto(reviewEntity))
                .collect(Collectors.toList());
    }

    // GET A REVIEW BY ID CLIENT
    public List<ReviewDto> findAllByIdClientReview(int id){

        Optional<ClientEntity> client = clientRepository.findById(id);

        if (client.isEmpty()) {
            throw ReviewException.clientNotFound();
        }
        List<ReviewEntity> reviewEntities = client.get().getReviews();
        if (reviewEntities.isEmpty()) {
            throw ReviewException.reviewWithIdClientNotFound();
        }

        return reviewEntities.stream()
                .map(reviewEntity -> entityToDto(reviewEntity))
                .collect(Collectors.toList());
    }


    //  GET A REVIEW BY ID
    public ReviewDto getReviewById(int id) {

        ReviewEntity reviewEntity = reviewRepository.findByIdReview(id);
        if (reviewEntity == null)
        {
            throw ReviewException.reviewNotFound();
        }
        return entityToDto(reviewEntity);
    }


    //  UPDATE REVIEW'S INFO => PUT
    public ReviewDto updateReview(int id, ReviewDto review){
        ReviewEntity reviewEntity = reviewRepository.findById(id).orElse(null);
        if(reviewEntity == null){
            throw ReviewException.reviewWithNoId();
        }
        else if(review.getRate() > 5){
            throw ReviewException.rateTooHigh();
        }
        else if(review.getDescription() == null || review.getSubject() == null || review.getRate() == null) {
            throw ReviewException.fieldsNull();
        }

        // doar cele 3 campuri pe care vreau sa le modific
        reviewEntity.setDescription(review.getDescription());
        reviewEntity.setRate(review.getRate());
        reviewEntity.setSubject(review.getSubject());

        return entityToDto(reviewRepository.save(reviewEntity));
    }

    //  DELETE A REVIEW
    public void deleteReview(int id){
        ReviewEntity reviewEntity = reviewRepository.findById(id).orElse(null);
        if(reviewEntity == null){
            throw ReviewException.reviewWithNoId();
        }
        reviewRepository.deleteById(id);
    }
}
