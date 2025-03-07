package edu.alexandra.pet.controller.request.pet;

import edu.alexandra.pet.model.PetActivity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UpdatePetRequest {

    @NotBlank(message = "Activity is required")
    private final PetActivity activity;
}
