package eu.senlainc.course.foodpricing.dto;

import lombok.Data;

@Data
public class ApiResponse {

    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }
}