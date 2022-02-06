package kd.prac.tdd.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult implements ErrorResult{
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    POINT_IS_NULL(HttpStatus.BAD_REQUEST, "Point Is Not Null"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not Found"),
    UNKNOWN_USER(HttpStatus.BAD_REQUEST, "Unknown User" );

    private final HttpStatus httpStatus;
    private final String message;

}
