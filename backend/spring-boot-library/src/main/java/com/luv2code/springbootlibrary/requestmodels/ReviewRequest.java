package com.luv2code.springbootlibrary.requestmodels;

import lombok.Data;

import java.util.Optional;

/**
 * The purpose of the ReviewRequest class is to serve as a data transfer object (DTO) used to receive and convey information about book reviews from client requests to the server for processing, validation, and manipulation.
 */
@Data
public class ReviewRequest {
    private double rating;

    private Long bookId;

    private Optional<String> reviewDescription;
}
