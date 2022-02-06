package kd.prac.tdd.controller;

import kd.prac.tdd.dto.*;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.service.MembershipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.lang.reflect.Member;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MembershipController {

    private static final String USER_ID_HEADER = "X-USER-ID";

    private final MembershipService membershipService;

    @ExceptionHandler(MembershipException.class)
    public ResponseEntity handleMembershipException(MembershipException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorResult());
        return ResponseEntity.status(ex.getErrorResult().getHttpStatus()).body(response);
    }


    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId, // 검증 필요 없이 값이 없으면 400 반환
            @RequestBody @Validated final MembershipRequest request
    ) {
        MembershipResponse membershipResponse = membershipService.addMembership(userId, request.getMembershipType(), request.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
    }

    @GetMapping("/api/v1/membership/list")
    public ResponseEntity<List<MembershipDetail>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) String userId
    ) {

        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/membership")
    public ResponseEntity<MembershipDetail> getMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @RequestParam("type") MembershipType type
    ) {
        log.info("getMembership() userId={}, type={}", userId, type);

        return ResponseEntity.ok(membershipService.getMembership(userId, type));
    }

    @DeleteMapping("/api/v1/membership/{membershipId}")
    public ResponseEntity removeMembership(
            @RequestHeader(USER_ID_HEADER) String userId,
            @PathVariable("membershipId") Long membershipId
    ) {
        log.info("removeMembership() userId={}, membershipId={}", userId, membershipId);
        membershipService.removeMembership(membershipId, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/membership/accumulate")
    public ResponseEntity accumulatePoint(
            @RequestHeader(USER_ID_HEADER) String userId,
            @RequestBody @Validated PointAccRequest request
    ) {
        log.info("accPoint() userId={}, membershipId={}, point={}", userId, request.getMembershipId(), request.getPrice());

        membershipService.accumulatePoint(userId, request.getMembershipId(), request.getPrice());

        return ResponseEntity.ok().build();
    }

}
