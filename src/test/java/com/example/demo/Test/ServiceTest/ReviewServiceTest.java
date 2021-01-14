package com.example.demo.Test.ServiceTest;

import com.example.demo.dto.ClientDto;
import com.example.demo.dto.HotelDto;
import com.example.demo.dto.ReviewDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.entity.ReviewEntity;
import com.example.demo.exception.ReviewException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.HotelRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.service.ReviewService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {


    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReviewService reviewService;

    private static ReviewDto reviewDto1;
    private static ReviewDto reviewDto2;
    private static ReviewDto reviewDto3;
    private static ClientDto clientDto;
    private static HotelDto hotelDto;

    private static ReviewEntity reviewEntity;

    private static List<ReviewEntity> reviewEntities;
    private static List<ReviewDto> reviewDtos;
    private static Set<RoomDto> roomDtos;

    private static String description;
    private static String subject;
    private static Integer rate;


    @BeforeAll
    public static void setup(){
        reviewDto1 = new ReviewDto(13, "review hotel", "hotel excelent",
                3, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");

        // reviewDto2 are rate setat gresit pentru a realiza testul de rate
        reviewDto2 = new ReviewDto(13, "review hotel", "hotel excelent",
                7, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");

        // reviewDto3 are description setat null pentru a realiza testul de fields null
        reviewDto3 = new ReviewDto(13, "review hotel", null,
                5, 21, "Ionel", "Pop", 8,
                "Iasi", "Strada Victoriei 54");

        reviewDtos = new ArrayList<>();
        reviewDtos.add( new ReviewDto(reviewDto1.getIdReview(), reviewDto1.getSubject(), reviewDto1.getDescription(),
                reviewDto1.getRate(), reviewDto1.getIdClientReview(), reviewDto1.getClientLastName(), reviewDto1.getClientFirstName(),
                reviewDto1.getIdHotelReview(), reviewDto1.getHotelCity(), reviewDto1.getHotelAddress()) );


        roomDtos = new HashSet<>();

        clientDto = new ClientDto(21, "Ionel", "Pop", "IOPOP", "3902485324", reviewDtos, null, null);

        hotelDto = new HotelDto(8, "Strada Victoriei 54", "Iasi", "54654645", 34, roomDtos, reviewDtos);

        rate = 3;
        description = "ok";
        subject = "rev";
    }


    @Test
    @DisplayName("Test invalid rate")
    public void testInvalidRate(){
        reviewEntity = reviewService.dtoToEntity(reviewDto2);
        reviewEntity.setIdReview(reviewDto2.getIdReview());
        Mockito.lenient().when(reviewRepository.save(reviewEntity)).thenReturn(reviewEntity);
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.addReview(reviewDto2));
        assertEquals(ReviewException.ReviewErrors.REVIEW_RATE_TOO_HIGH, exception.getError());
    }

    @Test
    @DisplayName("Test reviews not found")
    public void testGetReviews(){
        reviewEntities = reviewDtos.stream()
                   .map(reviewDto -> reviewService.dtoToEntity(reviewDto))
                   .collect(Collectors.toList());

        when(reviewRepository.findAll()).thenReturn(reviewEntities);
        ReviewException exception = assertThrows(ReviewException.class, () ->
                    reviewService.getReviews(subject, description, rate));
        assertEquals(ReviewException.ReviewErrors.REVIEW_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test review with the given id not found")
    public void testGetReviewById(){
        reviewEntity = reviewService.dtoToEntity(reviewDto2);
        reviewEntity.setIdReview(reviewDto2.getIdReview());
        Mockito.lenient().when(reviewRepository.findByIdReview(13)).thenReturn(reviewEntity);
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.getReviewById(10));
        assertEquals(ReviewException.ReviewErrors.REVIEW_DOES_NOT_EXIST, exception.getError());
    }

    @Test
    @DisplayName("Test review with hotel not found")
    public void testFindReviewByIdHotel(){
        doReturn(Optional.empty()).when(hotelRepository).findById(reviewDto2.getIdHotelReview());
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.findAllByIdHotelReview(8));
        assertEquals(ReviewException.ReviewErrors.HOTEL_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("Test hotel without reviews")
    public void testFindAllByIdHotelReview(){
        reviewEntities = reviewDtos.stream()
                .map(reviewDto -> reviewService.dtoToEntity(reviewDto))
                .collect(Collectors.toList());
        Mockito.lenient().when(reviewRepository.findAll()).thenReturn(reviewEntities);
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.findAllByIdHotelReview(10));
        assertEquals(ReviewException.ReviewErrors.HOTEL_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("Test review with client not found")
    public void testFindAllByIdClientReview(){
        doReturn(Optional.empty()).when(clientRepository).findById(reviewDto2.getIdClientReview());
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.findAllByIdClientReview(21));
        assertEquals(ReviewException.ReviewErrors.CLIENT_NOT_FOUND, exception.getError());
    }

    @Test
    @DisplayName("Test review update rate too high")
    public void testUpdateReviewRateTooHigh(){

        reviewEntity = reviewService.dtoToEntity(reviewDto1);
        reviewEntity.setIdReview(reviewDto1.getIdReview());
        Mockito.lenient().when(reviewRepository.findById(13)).thenReturn(Optional.ofNullable(reviewEntity));
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.updateReview(13, reviewDto2));
        assertEquals(ReviewException.ReviewErrors.REVIEW_RATE_TOO_HIGH, exception.getError());
    }

    @Test
    @DisplayName("Test review update id not found")
    public void testUpdateReviewById(){
        reviewEntity = reviewService.dtoToEntity(reviewDto1);
        reviewEntity.setIdReview(reviewDto1.getIdReview());
        Mockito.lenient().when(reviewRepository.findById(13)).thenReturn(Optional.empty());
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.updateReview(13, reviewDto2));
        assertEquals(ReviewException.ReviewErrors.REVIEW_DOES_NOT_EXIST, exception.getError());
    }
    @Test
    @DisplayName("Test review update fields null")
    public void testUpdateReviewFieldsNull(){
        reviewEntity = reviewService.dtoToEntity(reviewDto1);
        reviewEntity.setIdReview(reviewDto1.getIdReview());
        Mockito.lenient().when(reviewRepository.findById(reviewDto1.getIdReview())).thenReturn(Optional.ofNullable(reviewEntity));
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.updateReview(13, reviewDto3));
        assertEquals(ReviewException.ReviewErrors.REVIEW_COULD_NOT_BE_UPDATED, exception.getError());
    }

    @Test
    @DisplayName("Test review not deleted")
    public void testDeleteReview(){
        reviewEntity = reviewService.dtoToEntity(reviewDto2);
        reviewEntity.setIdReview(reviewDto2.getIdReview());
        Mockito.lenient().when(reviewRepository.findById(13)).thenReturn(Optional.ofNullable(reviewEntity));
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.deleteReview(10));
        assertEquals(ReviewException.ReviewErrors.REVIEW_DOES_NOT_EXIST, exception.getError());
    }

}
