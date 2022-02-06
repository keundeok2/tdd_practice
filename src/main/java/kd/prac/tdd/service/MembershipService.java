package kd.prac.tdd.service;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipDetail;
import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PointService ratedPointService;

    public MembershipResponse addMembership(String userId, MembershipType type, int point) {

        Optional<Membership> optFindMembership = membershipRepository.findByUserIdAndMembershipType(userId, type);

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

    public List<MembershipDetail> getMembershipList(String userId) {

        List<Membership> findList = membershipRepository.findByUserId(userId);
        if (findList.isEmpty()) {
            throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
        }

        return findList.stream().map(MembershipDetail::new).collect(Collectors.toList());
    }

    public MembershipDetail getMembership(String userId, MembershipType type) {

        Optional<Membership> optMembership = membershipRepository.findByUserIdAndMembershipType(userId, type);
        return new MembershipDetail(
                optMembership.orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND)));
    }

    public void removeMembership(Long membershipId, String userId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.UNKNOWN_USER);
        }

        membershipRepository.deleteById(membership.getId());
    }

    @Transactional
    public void accumulatePoint(String userId, Long membershipId, int price) {
        Membership findMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!findMembership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.UNKNOWN_USER);
        }

        int calculate = ratedPointService.calcultate(price);
        findMembership.addPoint(calculate);

    }
}
