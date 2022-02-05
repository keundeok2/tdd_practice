package kd.prac.tdd.service;

import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.repository.MembershipRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    MembershipService membershipService;

    @Mock
    MembershipRepository membershipRepository;

    String userId = "userId";
    MembershipType type = MembershipType.NAVER;
    int point = 10000;

    @Test
    @DisplayName("멤버십등록실패_이미존재")
    void create_membership_result_fail_already_exists() {
        // given
//        when(membershipRepository.findByUserIdAndMembershipName(userId, type)).thenReturn(Optional.of(Membership.builder().build()));
        doReturn(Optional.of(Membership.builder().build())).when(membershipRepository).findByUserIdAndMembershipName(userId, type);


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

        doReturn(Optional.empty()).when(membershipRepository).findByUserIdAndMembershipName(userId, type);
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
        verify(membershipRepository, times(1)).findByUserIdAndMembershipName(userId, type);
        verify(membershipRepository, times(1)).save(any(Membership.class));

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