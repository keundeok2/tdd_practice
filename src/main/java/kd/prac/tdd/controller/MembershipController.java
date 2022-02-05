package kd.prac.tdd.controller;

import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipRequest;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Member;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private static final String USER_ID_HEADER = "X-USER-ID";

    private final MembershipService membershipService;

    @ExceptionHandler(MembershipException.class)
    public ResponseEntity handleMembershipException(MembershipException ex) {
        return ResponseEntity.status(ex.getErrorResult().getHttpStatus()).build();
    }


    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId, // 검증 필요 없이 값이 없으면 400 반환
            @RequestBody @Validated final MembershipRequest request
            ) {

        MembershipResponse membershipResponse = membershipService.addMembership(userId, request.getMembershipType(), request.getPoint());


        return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
    }

}
