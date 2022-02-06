package kd.prac.tdd.dto;


import kd.prac.tdd.enums.ErrorResult;
import kd.prac.tdd.enums.MembershipErrorResult;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse(ErrorResult errorResult) {
        this.message = errorResult.getMessage();
        this.status = errorResult.getHttpStatus().value();
    }
}
