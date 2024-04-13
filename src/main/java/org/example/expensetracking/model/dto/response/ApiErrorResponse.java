package org.example.expensetracking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private String type;
    private String title;
    private HttpStatus status;
    private Integer code;
    private String instance;
    private Date timestamp;
    private Map<String, String> errors;
}

//  Example:
//          Map<String, String> errorMap = new HashMap<>();
//          errorMap.put("error", e.getMessage() != null ? e.getMessage() : "Unknown error occurred");
//
//          ErrorResponse errorResponse = new ErrorResponse(
//                "about:blank",
//                "Internal Server Error",
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                "/api/v1/attendees/" + id,
//                new Date(),
//                errorMap
//          );
//
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);


