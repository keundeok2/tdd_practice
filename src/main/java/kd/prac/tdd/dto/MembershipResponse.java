package kd.prac.tdd.dto;

import kd.prac.tdd.enums.MembershipType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MembershipResponse {

    private Long id;
    private MembershipType membershipType;

    @Builder
    public MembershipResponse(Long id, MembershipType membershipType) {
        this.id = id;
        this.membershipType = membershipType;
    }
}
