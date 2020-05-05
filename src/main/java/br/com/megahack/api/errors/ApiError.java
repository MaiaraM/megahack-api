package br.com.megahack.api.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ApiError{

    private HttpStatus httpStatus;
    private String message;
    private String debugMessage;
    // ISO-8601
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    private String requestTraceId;
    private List<ApiSubError> subErrors;


    public ApiError(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus errorCode, String errorMessage) {
        this();
        this.httpStatus = errorCode;
        this.message = errorMessage;
    }

}
