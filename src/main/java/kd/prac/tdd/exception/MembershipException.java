package kd.prac.tdd.exception;

import kd.prac.tdd.dto.MembershipErrorResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MembershipException extends RuntimeException{

    private final MembershipErrorResult errorResult;
}
