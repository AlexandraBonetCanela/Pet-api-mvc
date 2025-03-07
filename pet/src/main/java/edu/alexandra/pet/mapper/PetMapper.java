package edu.alexandra.pet.mapper;

import edu.alexandra.pet.entity.PetEntity;
import edu.alexandra.pet.model.pet.Pet;
import edu.alexandra.pet.model.pet.PetState;
import edu.alexandra.pet.model.pet.PetType;

public class PetMapper {

    public static PetEntity toEntity(Pet pet, String userId) {

        return new PetEntity(
                pet.getId(),
                pet.getName(),
                pet.getType().toString(),
                pet.getHappinessLevel(),
                pet.getFoodLevel(),
                pet.getState().toString(),
                pet.getBackgroundImage(),
                pet.getLastUpdated(),
                userId
        );
    }

    public static Pet toDomain(PetEntity entity) {

        return new Pet(
                entity.getId(),
                entity.getName(),
                PetType.valueOf(entity.getType()),
                entity.getHappinessLevel(),
                entity.getFoodLevel(),
                PetState.valueOf(entity.getState()),
                entity.getBackgroundImage(),
                entity.getLastUpdated()
        );
    }
}
