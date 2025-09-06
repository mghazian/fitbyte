package com.coffeeteam.fitbyte.profileManagement.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.IOException;

@Data
public class UserUpdateDto {

    @Pattern(regexp = "CARDIO|WEIGHT", message = "Preference must be CARDIO or WEIGHT")
    @NotBlank
    private String preference;

    @NotBlank
    @Pattern(regexp = "KG|LBS", message = "Weight unit must be KG or LBS")
    private String weightUnit;

    @NotBlank
    @Pattern(regexp = "CM|INCH", message = "Height unit must be CM or INCH")
    private String heightUnit;

    @NotNull
    @Min(value = 10, message = "Weight must be at least 10")
    @Max(value = 1000, message = "Weight must not exceed 1000")
    private Integer weight;

    @NotNull
    @Min(value = 3, message = "Height must be at least 3")
    @Max(value = 250, message = "Height must not exceed 250")
    private Integer height;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 60, message = "Name must be between 2 and 60 characters")
    @Pattern(regexp = "^[a-zA-Z\\s'.]+$", message = "Name must contain only letters, spaces, apostrophes, and periods")
    @JsonDeserialize(using = StringOnlyDeserializer.class)
    private String name;



    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(https?|ftp)://[a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(\\.([a-zA-Z0-9]([a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?))*\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$",
            message = "Must be a complete and valid URL with proper domain")
    private String imageUri;
}


class StringOnlyDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isBoolean()) {
            throw new IllegalArgumentException("Name must be a string value, not boolean");
        }

        if (!node.isTextual()) {
            throw new IllegalArgumentException("Name must be a string value");
        }

        return node.asText();
    }
}
