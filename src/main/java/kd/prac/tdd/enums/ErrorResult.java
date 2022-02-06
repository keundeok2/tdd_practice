package kd.prac.tdd.enums;

import org.springframework.http.HttpStatus;

public interface ErrorResult {
    String getMessage();
    HttpStatus getHttpStatus();
}
