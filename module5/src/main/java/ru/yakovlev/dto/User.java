package ru.yakovlev.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    @JsonProperty
    private final Integer id;
    @JsonProperty
    private final String name;
}