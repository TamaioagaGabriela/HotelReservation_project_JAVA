package com.example.demo.controller;

import com.example.demo.dto.ReviewDto;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ADD REVIEW => POST
    @PostMapping
    public ResponseEntity<ReviewEntity> addReview (@Valid @RequestBody ReviewDto review)
    {
        log.info("Received");
        ReviewEntity reviewEntity = reviewService.addReview(review);
        return new ResponseEntity<>(reviewEntity, HttpStatus.CREATED);
    }


    //  GET ALL REVIEWS USING DIFFERENT PARAMS
    @GetMapping
    public ResponseEntity<Iterable<ReviewDto>> getReviews(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer rate) {

        List<ReviewDto> reviewDtos = reviewService.getReviews(subject, description, rate);
        return reviewDtos.isEmpty() ?
                ResponseEntity.noContent().build()
                : ResponseEntity.ok(reviewDtos);
    }

    // GET REVIEWS BY ID HOTEL
    @GetMapping(params = "idHotel")
    public ResponseEntity<Iterable<ReviewDto>> getReviewsByIdHotel(@RequestParam(required = false) int idHotel) {
        List<ReviewDto> reviewDtos;

        if(idHotel == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hotel does not exists in the database");
        }
        else
        {log.info("Received request to get all the reviews by idHotel", idHotel);
            reviewDtos = reviewService.findAllByIdHotelReview(idHotel);
        }
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    @GetMapping(params = "idClient")
    public ResponseEntity<Iterable<ReviewDto>> getReviewsByIdClient(@RequestParam(required = false) int idClient) {
        List<ReviewDto> reviewDtos;
        if(idClient == 0){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Client does not exists in the database");
        }
        else
        {log.info("Received request to get all the reviews by idClient", idClient);
            reviewDtos = reviewService.findAllByIdClientReview(idClient);
        }
        return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
    }

    //  GET A REVIEW BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable int id) {
        log.info("Received request to get employee with id: {}", id);
        ReviewDto reviewDto = reviewService.getReviewById(id);
        log.info("Created returnedEmployeeEntity with id: {}", id);
        if (reviewDto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee with campaign id: " + id + " not found in the database");
        }
        log.debug("before return");
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    //  UPDATE REVIEW'S INFO => PUT
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable int id, @RequestBody ReviewDto review){
        ReviewDto reviewDto = reviewService.updateReview(id, review);
        log.info(String.valueOf(reviewDto));
        if(reviewDto == null){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Review with id: " + id + " not found in the database");
        }
        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    //  DELETE A REVIEW
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable int id) {

        reviewService.deleteReview(id);
    }
}
