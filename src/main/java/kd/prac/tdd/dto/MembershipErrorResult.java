package kd.prac.tdd.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MembershipErrorResult {
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    POINT_IS_NULL(HttpStatus.BAD_REQUEST, "POINT IS NOT NULL");

    private final HttpStatus httpStatus;
    private final String message;


}
