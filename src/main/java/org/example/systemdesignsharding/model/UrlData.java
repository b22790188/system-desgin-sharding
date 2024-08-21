package org.example.systemdesignsharding.model;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlData {
    String long_url;
    String short_url;
    int ttl;
}
