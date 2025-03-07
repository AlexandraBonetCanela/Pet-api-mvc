package edu.alexandra.pet.service.impl;

import edu.alexandra.pet.controller.request.pet.CreatePetRequest;
import edu.alexandra.pet.controller.request.pet.UpdatePetRequest;
import edu.alexandra.pet.entity.PetEntity;
import edu.alexandra.pet.exception.DatabaseException;
import edu.alexandra.pet.mapper.PetMapper;
import edu.alexandra.pet.model.pet.Pet;
import edu.alexandra.pet.model.pet.PetState;
import edu.alexandra.pet.repository.PetRepository;
import edu.alexandra.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public Pet createPet(CreatePetRequest createPetRequest) {

        Pet pet = new Pet(createPetRequest.getName(), createPetRequest.getType());

        try {
            return PetMapper.toDomain(petRepository.save(PetMapper.toEntity(pet, createPetRequest.getUserId())));
        } catch (Exception e ) {
            throw new DatabaseException("An unexpected error occurred while creating pet");
        }

    }

    @Override
    public void deletePet(String petId) {
        if(!petRepository.existsById(petId)) {
            throw new DatabaseException("Pet with ID " + petId + " not found");
        }
       petRepository.deleteById(petId);
    }

    @Override
    public List<Pet> getPets(String userId) {

        List<Pet> pets = petRepository.findByUserId(userId)
                .stream()
                .map(PetMapper::toDomain)
                .toList();

        pets.forEach(this::updateStats);

        return pets;
    }

    @Override
    public Pet updatePet(String petId, UpdatePetRequest updatePetRequest) {

        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new DatabaseException("Pet not found"));

        Pet pet = PetMapper.toDomain(petEntity);

        switch (updatePetRequest.getActivity()) {
            case FEED:
                feed(pet);
                break;
            case PLAY:
                play(pet);
                break;
            case SLEEP:
                sleep(pet);
                break;
        }

        return PetMapper.toDomain(petRepository.save(PetMapper.toEntity(pet, petEntity.getUserId())));

    }

    @Override
    public List<Pet> getAllPets() {

        return petRepository.findAll()
                .stream()
                .map(PetMapper::toDomain)
                .toList();
    }

    @Override
    public Pet updatePetBackground(String petId, String newPetBackground) {

        PetEntity petEntity = petRepository.findById(petId)
                .orElseThrow(() -> new DatabaseException("Pet not found"));


        if (newPetBackground == null || newPetBackground.isEmpty()) {
            throw new IllegalArgumentException("Background image cannot be null or empty");
        }

        petEntity.setBackgroundImage(newPetBackground);

        return PetMapper.toDomain(petRepository.save(petEntity));
    }

    private void feed(Pet pet) {
        pet.setFoodLevel(Math.min(5, pet.getFoodLevel() + 1));
        pet.setHappinessLevel(Math.min(5, pet.getHappinessLevel() + 1));
        updateTimestamp(pet);
        log.info("Pet {} has been fed", pet.getName());
    }

    private void play(Pet pet) {
        pet.setHappinessLevel( Math.min(5, pet.getHappinessLevel() + 1));
        pet.setFoodLevel( Math.max(0, pet.getFoodLevel() - 1));
        updateTimestamp(pet);
        log.info("Pet {} has played", pet.getName());
    }

    private void sleep(Pet pet) {
        pet.setHappinessLevel(Math.max(0, pet.getHappinessLevel() - 1));
        pet.setFoodLevel(Math.max(0, pet.getFoodLevel() - 1));
        updateTimestamp(pet);
        log.info("Pet {} has slept", pet.getName());
    }

    public void updateStats(Pet pet) {
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = java.time.Duration.between(pet.getLastUpdated(), now).toMinutes();

        if (minutesPassed >= 10) {
            pet.setHappinessLevel(Math.max(0, pet.getHappinessLevel() - 1)) ;
            pet.setFoodLevel(Math.max(0, pet.getFoodLevel() - 1));
            pet.setState(PetState.SLEEPING);
            updateTimestamp(pet);
            log.info("⏳ Pet '{}' stats auto-updated. New happinessLevel: {}, foodLevel: {}", pet.getName(), pet.getHappinessLevel(), pet.getFoodLevel());
        }
    }

    private void updateTimestamp(Pet pet) {
        pet.setLastUpdated(LocalDateTime.now());
        log.debug("📌 lastUpdated timestamp updated for pet '{}': {}", pet.getName(), pet.getLastUpdated());
    }
}
