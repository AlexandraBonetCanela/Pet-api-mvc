package edu.alexandra.pet.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class UpdatePetBackgroundRequest {

    @NotBlank(message = "New Background is required")
    private final String newPetBackground;
}
