package org.example.expensetracking.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiFileResponse<T> {
    private String message;
    private int code;
    private HttpStatus status;
    private T payload;

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private String message;
        private int code;
        private HttpStatus status;
        private T payload;

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> code(int code) {
            this.code = code;
            return this;
        }

        public ApiResponseBuilder<T> status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public ApiResponseBuilder<T> payload(T payload) {
            this.payload = payload;
            return this;
        }

        public ApiFileResponse<T> build() {
            return new ApiFileResponse<>(message, code, status, payload);
        }
    }
}
