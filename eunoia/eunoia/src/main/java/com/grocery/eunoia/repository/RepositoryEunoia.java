package com.grocery.eunoia.repository;

import com.grocery.eunoia.model.ModelEunoia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryEunoia extends JpaRepository<ModelEunoia, Long> {
    Optional<ModelEunoia> findByEmail(String email);
    void deleteByEmail(String email);
}
