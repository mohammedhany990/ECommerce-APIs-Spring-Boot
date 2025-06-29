package com.ECom.ECom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateProductBrandDto {
    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    private String brandName;

    @Size(max = 500, message = "Brand description cannot exceed 500 characters")
    private String brandDescription;
}
