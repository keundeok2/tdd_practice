package kd.prac.tdd.repository;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipDetail;
import kd.prac.tdd.enums.MembershipType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findByUserId(String userId);


}
