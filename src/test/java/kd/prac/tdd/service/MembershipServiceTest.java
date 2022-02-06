package kd.prac.tdd.service;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipDetail;
import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.repository.MembershipRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    MembershipService membershipService;

    @Mock
    MembershipRepository membershipRepository;

    @Mock
    RatedPointService ratedPointService;

    String userId = "userId";
    MembershipType type = MembershipType.NAVER;
    int point = 10000;

    @Test
    @DisplayName("멤버십등록실패_이미존재")
    void create_membership_result_fail_already_exists() {
        // given
//        when(membershipRepository.findByUserIdAndMembershipName(userId, type)).thenReturn(Optional.of(Membership.builder().build()));
        doReturn(Optional.of(Membership.builder().build())).when(membershipRepository).findByUserIdAndMembershipType(userId, type);


        // when
        MembershipException result = assertThrows(
                MembershipException.class,
                () -> membershipService.addMembership(userId, type, point));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    @DisplayName("멤버십 등록 성공")
    void create_membership_result_success() {
        // given
//        when(membershipRepository.findByUserIdAndMembershipName(userId, type)).thenReturn(Optional.empty());
//        when(membershipRepository.save(BDDMockito.any(Membership.class))).thenReturn(membership());

        doReturn(Optional.empty()).when(membershipRepository).findByUserIdAndMembershipType(userId, type);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        // when
        MembershipResponse result = membershipService.addMembership(userId, type, point);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getMembershipType()).isEqualTo(type);

        // verify
        /*
            stub된 레퍼지터리의 메서드가 몇 번 호출되었는지 검증
         */
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, type);
        verify(membershipRepository, times(1)).save(any(Membership.class));

    }

    @Test
    @DisplayName("멤버십 리스트 조회 테스트")
    void membership_not_found_fail_test() {
        // given
        given(membershipRepository.findByUserId(userId))
                .willReturn(Arrays.asList(membership(), membership()));
        // when
        List<MembershipDetail> membershipList = membershipService.getMembershipList(userId);

        // then
        assertThat(membershipList).isNotNull();
        assertThat(membershipList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버십 상세조회 실패 테스트_멤버십 존재하지 않음")
    void membership_detail_failed_test() {
        // given
        given(membershipRepository.findByUserIdAndMembershipType(userId, type))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> membershipService.getMembership(userId, type))
                .isInstanceOf(MembershipException.class)
                .extracting("errorResult")
                .isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    @DisplayName("멤버십 상세조회 성공 테스트")
    void membership_detail_success_test() {
        // given
        Membership givenMembership = Membership.builder().build();

        given(membershipRepository.findByUserIdAndMembershipType(userId, type))
                .willReturn(Optional.ofNullable(givenMembership));

        // when
        MembershipDetail detail = membershipService.getMembership(userId, type);

        // then
        assertThat(detail).isNotNull();

    }

    @Test
    @DisplayName("멤버십 삭제 실패 테스트_멤버십 존재하지 않음")
    void membership_remove_fail_test() {
        // given
        final Long membershipId = 1L;
        given(membershipRepository.findById(membershipId)).willReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> membershipService.removeMembership(membershipId, userId))
                .isInstanceOf(MembershipException.class);
    }

    @Test
    @DisplayName("멤버십 삭제 실패 테스트_유저가 일치하지 않음")
    void membership_remove_fail_unknown_user_test() {
        // given
        final Long membershipId = 1L;
        final Membership givenMembership = Membership.builder()
                .userId("user22")
                .build();

        given(membershipRepository.findById(membershipId)).willReturn(Optional.of(givenMembership));
        // when
        // then
        assertThatThrownBy(() -> membershipService.removeMembership(membershipId, userId))
                .isInstanceOf(MembershipException.class)
                .extracting("errorResult")
                .isEqualTo(MembershipErrorResult.UNKNOWN_USER);
    }

    @Test
    @DisplayName("멤버십 삭제 성공 테스트")
    void membership_remove_success_test() {
        // given
        final Long membershipId = 1L;
        final Membership givenMembership = Membership.builder()
                .userId(userId)
                .build();

        given(membershipRepository.findById(membershipId)).willReturn(Optional.of(givenMembership));

        // when
        membershipService.removeMembership(membershipId, userId);

        // then
    }

    @Test
    @DisplayName("멤버십적립_실패_존재하지_않음")
    void membership_point_fail_test_not_exist() {
        // given
        String userId = "user";
        Long membershipId = 1L;
        int price = 1000;

        given(membershipRepository.findById(membershipId)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> membershipService.accumulatePoint(userId, membershipId, price))
                .isInstanceOf(MembershipException.class)
                .extracting("errorResult")
                .isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);

    }

    @Test
    @DisplayName("멤버십적립_실패_유저가_다름")
    void membership_point_fail_test_user_diff() {
        // given
        String userId = "user";
        Long membershipId = 1L;
        int price = 1000;

        Membership givenMembership = Membership.builder()
                .id(membershipId)
                .userId(userId)
                .membershipType(MembershipType.NAVER)
                .point(100)
                .build();

        given(membershipRepository.findById(membershipId)).willReturn(Optional.of(givenMembership));

        // when
        // then
        assertThatThrownBy(() -> membershipService.accumulatePoint("user2", membershipId, price))
                .isInstanceOf(MembershipException.class)
                .extracting("errorResult")
                .isEqualTo(MembershipErrorResult.UNKNOWN_USER);

    }


    @Test
    @DisplayName("포인트_적립_성공")
    void membership_point_success_test() {
        // given
        String userId = "user";
        Long membershipId = 1L;
        int price = 1000;

        Membership givenMembership = Membership.builder()
                .id(membershipId)
                .userId(userId)
                .membershipType(MembershipType.NAVER)
                .point(100)
                .build();

        given(membershipRepository.findById(membershipId)).willReturn(Optional.of(givenMembership));
        // when

        membershipService.accumulatePoint(userId, membershipId, price);
        // then
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(type)
                .build()
                ;
    }


}