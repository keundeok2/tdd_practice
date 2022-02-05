package kd.prac.tdd.service;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(String userId, MembershipType type, int point) {

        Optional<Membership> optFindMembership = membershipRepository.findByUserIdAndMembershipName(userId, type);

        optFindMembership.ifPresent((membership) -> {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        });

        final Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(type)
                .point(point)
                .build();

        Membership savedMembership = membershipRepository.save(membership);

        return MembershipResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }
}
