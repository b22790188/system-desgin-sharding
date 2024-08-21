package org.example.systemdesignsharding.model;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class shortenRequest {
    @NotNull(message = "URL cannot be null")
    private String longUrl;
    @NotNull(message = "ttl cannot be null")
    private Integer ttl;
}
