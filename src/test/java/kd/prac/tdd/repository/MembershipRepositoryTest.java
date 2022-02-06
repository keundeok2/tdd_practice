package kd.prac.tdd.repository;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.enums.MembershipType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class MembershipRepositoryTest {


    @Autowired
    MembershipRepository membershipRepository;

    @Test
    @DisplayName("MembershipRespository가 null이 아님을 확인")
    void membershipRepository_is_not_null() {
        assertThat(membershipRepository).isNotNull();
        /*
            코드 작성 순서
         1. MemberRepository 주입
         2. 테스트코드 작성 assertThat(membershipRepository).isNotNull();
         3. 컴파일 에러 발생
         4. 컴파일 에러를 해결하기 위하여 MemberRepository 인터페이스 생성
         5. 테스트 -> 실패 -> 엔터티 필요
         6. 엔터티 클래스 생성
         7. 테스트 -> 실패 -> 엔터티 ID 컬럼 추가, 기본 생성자 추가
         6. 테스트 -> 성공
         */
    }

    @Test
    @DisplayName("멤버십 등록")
    void create_membership() {
        // given
        final Membership membership = Membership.builder()
                .userId("user")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        Membership savedResult = membershipRepository.save(membership);

        // then
        assertThat(savedResult.getId()).isNotNull();
        assertThat(savedResult.getUserId()).isEqualTo("user");
        assertThat(savedResult.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(savedResult.getPoint()).isEqualTo(10000);

        /*
            작성 순서
            1. 테스트코드 작성 (given, when, then)
            2. Membership 엔터티 작성
            3. 테스트
            4. 멤버십 이름을 enum으로 변경
            5. 엔터티 변경
            6. enum 생성
            7. 테스트
         */
    }

    @Test
    @DisplayName("멤버십 존재 확인")
    void is_exist_membership() {
        // given
        final Membership membership = Membership.builder()
                .userId("user")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        membershipRepository.save(membership);
        final Membership findMembership = membershipRepository.findByUserIdAndMembershipType("user", MembershipType.NAVER).orElseThrow();

        // then
        assertThat(findMembership).isNotNull();
        assertThat(findMembership.getId()).isNotNull();
        assertThat(findMembership.getUserId()).isEqualTo(membership.getUserId());
        assertThat(findMembership.getMembershipType()).isEqualTo(membership.getMembershipType());
        assertThat(findMembership.getPoint()).isEqualTo(membership.getPoint());

        /*
            작성 순서
            1. 테스트 코드 작성
            2. MemberRepository 쿼리 메서드 작성
            3. 테스트
         */

    }

    @Test
    @DisplayName("멤버십 리스트가 0")
    void membership_list_size_0() {
        // given
        String userId = "123";
        // when
        List<Membership> results = membershipRepository.findByUserId(userId);
        // then
        assertThat(results.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("멤버십 리스트가 2")
    void membership_list_size_2() {
        // given
        String userId = "123";
        Membership membership1 = Membership.builder()
                .userId(userId)
                .membershipType(MembershipType.NAVER)
                .point(1000)
                .build();
        Membership membership2 = Membership.builder()
                .userId(userId)
                .membershipType(MembershipType.LINE)
                .point(1000)
                .build();
        membershipRepository.save(membership1);
        membershipRepository.save(membership2);
        // when
        List<Membership> results = membershipRepository.findByUserId(userId);

        // then
        assertThat(results.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버십 추가 후 삭제 테스트")
    void register_membership_and_delete_test() {
        // given
        Membership givenMembership = Membership.builder()
                .userId("123")
                .membershipType(MembershipType.NAVER)
                .point(1000)
                .build();
        Membership savedMembership = membershipRepository.save(givenMembership);

        // when
        membershipRepository.deleteById(savedMembership.getId());
        Optional<Membership> findMembership = membershipRepository.findById(savedMembership.getId());
        // then
        assertThatThrownBy(() -> findMembership.orElseThrow());
    }

    @Test
    @DisplayName("포인트 적립")
    void accumulate_point_failed_by_not_exists_membership() {
        // given
        Membership givenMembership = Membership.builder()
                .userId("123")
                .membershipType(MembershipType.NAVER)
                .point(100)
                .build();

        membershipRepository.save(givenMembership);

        // when


        // then
    }


}