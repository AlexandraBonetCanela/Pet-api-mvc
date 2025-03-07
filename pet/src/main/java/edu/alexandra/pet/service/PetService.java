package edu.alexandra.pet.service;

import edu.alexandra.pet.controller.request.CreatePetRequest;
import edu.alexandra.pet.controller.request.UpdatePetRequest;
import edu.alexandra.pet.model.Pet;

import java.util.List;

public interface PetService {

    Pet createPet(CreatePetRequest createPetRequest);

    void deletePet(String petId);

    List<Pet> getPets(String userId);

    Pet updatePet(String petId, UpdatePetRequest updatePetRequest);

    List<Pet> getAllPets();

    Pet updatePetBackground(String petId, String newPetBackground);
}
