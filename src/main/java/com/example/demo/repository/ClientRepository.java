package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {

    Iterable<ClientEntity> findByLastNameContaining(String lastName);
    ClientEntity findByIdClient(int id);
    Optional<ClientEntity> findByEmail(String email);

}
