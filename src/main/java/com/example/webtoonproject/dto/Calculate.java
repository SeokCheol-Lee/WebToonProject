package com.example.webtoonproject.dto;


import lombok.Builder;
import lombok.Data;

public class Calculate {

    @Data
    @Builder
    public static class AddCash{
        private Long cash;
    }

    @Data
    @Builder
    public static class UseCash{
        private String webtoonName;
        private Long cash;
    }

    @Data
    @Builder
    public static class ResponseCash{
        private Long cash;
    }
}
