package com.example.demo.repository;

import com.example.demo.entity.ReviewEntity;
import com.example.demo.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    ReviewEntity findByIdReview(int id);
}
