package com.example.webtoonproject.dto;


import lombok.Builder;
import lombok.Data;

public class Calculate {

    @Data
    @Builder
    public static class AddCash{
        private Long cash;
    }
}
