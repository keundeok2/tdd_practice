package kd.prac.tdd.repository;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.enums.MembershipType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MembershipRepositoryTest {


    @Autowired MembershipRepository membershipRepository;

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
                .membershipName(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        Membership savedResult = membershipRepository.save(membership);

        // then
        assertThat(savedResult.getId()).isNotNull();
        assertThat(savedResult.getUserId()).isEqualTo("user");
        assertThat(savedResult.getMembershipName()).isEqualTo(MembershipType.NAVER);
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
                .membershipName(MembershipType.NAVER)
                .point(10000)
                .build();

        // when
        membershipRepository.save(membership);
        final Membership findMembership = membershipRepository.findByUserIdAndMembershipName("user", MembershipType.NAVER).orElseThrow();

        // then
        assertThat(findMembership).isNotNull();
        assertThat(findMembership.getId()).isNotNull();
        assertThat(findMembership.getUserId()).isEqualTo(membership.getUserId());
        assertThat(findMembership.getMembershipName()).isEqualTo(membership.getMembershipName());
        assertThat(findMembership.getPoint()).isEqualTo(membership.getPoint());

        /*
            작성 순서
            1. 테스트 코드 작성
            2. MemberRepository 쿼리 메서드 작성
            3. 테스트
         */

    }




}