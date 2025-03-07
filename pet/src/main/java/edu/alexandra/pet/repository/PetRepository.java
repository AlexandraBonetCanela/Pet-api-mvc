package edu.alexandra.pet.repository;

import edu.alexandra.pet.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, String> {

    List<PetEntity> findByUserId(String userId);
}
