package kd.prac.tdd.dto;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.enums.MembershipType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class MembershipDetail {

    private Long id;
    private MembershipType membershipType;
    private Integer point;
    private LocalDateTime createdAt;

    public MembershipDetail(Membership membership) {
        this.id = membership.getId();
        this.membershipType = membership.getMembershipType();
        this.point = membership.getPoint();
        this.createdAt = membership.getCreatedAt();
    }

}
