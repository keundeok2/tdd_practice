package kd.prac.tdd.controller;

import com.google.gson.Gson;
import kd.prac.tdd.Entity.Membership;
import kd.prac.tdd.dto.MembershipDetail;
import kd.prac.tdd.dto.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipRequest;
import kd.prac.tdd.dto.MembershipResponse;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.repository.MembershipRepository;
import kd.prac.tdd.service.MembershipService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MembershipControllerTest {

    private MockMvc mockMvc;

    private Gson gson;

    @InjectMocks
    private MembershipController membershipController;

    @Mock
    private MembershipService membershipService;

    String userId = "userId";
    MembershipType type = MembershipType.NAVER;
    int point = 10000;
    private static final String USER_ID_HEADER = "X-USER-ID";

    @BeforeEach
    void setUp() {
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController).build();
    }

    @Test
    @DisplayName("mockMvc가 널이 아님")
    void mockMvc_not_null() {
        assertThat(membershipController).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    @DisplayName("멤버십등록실패_사용자헤더가없음")
    void not_exist_user_identity_header() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(type, point);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .content(gson.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("포인트가 null 실패 테스트")
    void point_is_null_fail_test() throws Exception {

        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(type, null);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .header(USER_ID_HEADER, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("포인트가 음수일 때 실패 테스트")
    void point_less_then_zero_fail_test() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(type, -1);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .header(USER_ID_HEADER, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십 NULL일 때 실패 테스트")
    void type_is_null_fail_test() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(null, 1);

        // when
        ResultActions resultActions = mockMvc.perform(
                post(url)
                        .header(USER_ID_HEADER, "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("서비스에서 Exception 발생한 경우 실패 테스트")
    void membershipservice_throw_exception_fail_test() throws Exception {

        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(MembershipType.NAVER, 100);

        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("user", MembershipType.NAVER, 100);
        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .header(USER_ID_HEADER, "user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request))
        ).andDo(print());

        // then
        result.andExpect(status().isBadRequest());


    }

    @Test
    @DisplayName("서비스에서 예외발생하여 실패 테스트")
    void exception_occured_fail_test() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(MembershipType.NAVER, 100);
//        BDDMockito.given(membershipService.addMembership("123", MembershipType.NAVER, 100)).willThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));
        given(membershipService.addMembership(any(String.class), any(MembershipType.class), any(Integer.class))).willThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));
        // when
        ResultActions result = mockMvc.perform(post(url)
                        .header("X-USER-ID", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andDo(print());
        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십 등록 성공")
    void register_membership_success() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(MembershipType.NAVER, 1000);
        final MembershipResponse response = new MembershipResponse(1L, MembershipType.NAVER);
        given(membershipService.addMembership("123", MembershipType.NAVER, 1000)).willReturn(response);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .header(USER_ID_HEADER, "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)));

        // then
        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(jsonPath("$.id", is(1)));
        resultActions.andExpect(jsonPath("$.membershipType", is("NAVER")));

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        MembershipResponse resultResponse = gson.fromJson(contentAsString, MembershipResponse.class);

        assertThat(resultResponse.getId()).isEqualTo(1);
        assertThat(resultResponse.getMembershipType()).isEqualTo(MembershipType.NAVER);

    }

    @Test
    @DisplayName("멤버십 리스트 조회 실패 _ 사용자 헤더 없음")
    void membership_list_failed_by_user_header_not_exist() throws Exception {
        // given
        final String url = "/api/v1/membership/list";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("멤버십 리스트 조회 성공 테스트 ")
    void membership_list_success_test() throws Exception {
        // given
        final String url = "/api/v1/membership/list";

        given(membershipService.getMembershipList(any())).willReturn(Arrays.asList(
                new MembershipDetail(),
                new MembershipDetail(),
                new MembershipDetail()
        ));

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .header(USER_ID_HEADER, "123")
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("멤버십 상세조회 실패 테스트_존재하지 않는 멤버십")
    void membership_not_found() throws Exception {
        // given
        final String url = "/api/v1/membership";
        given(membershipService.getMembership(any(), any()))
                .willThrow(new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        // when
        ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header(USER_ID_HEADER, "123")
                        .queryParam("type", "NAVER")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("멤버십 상세조회 성공")
    void membership_detail_success_test() throws Exception {
        // given
        final String url = "/api/v1/membership";
        given(membershipService.getMembership(any(), any())).willReturn(new MembershipDetail());

        // when
        ResultActions resultActions = mockMvc.perform(
                get(url)
                        .header(USER_ID_HEADER, "123")
                        .queryParam("type", "NAVER")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        resultActions.andExpect(status().isOk());

    }


    @Test
    @DisplayName("멤버십 삭제 실패 테스트 _ 헤더 없음")
    void membership_remove_fail_test_unknown_user() throws Exception {
        // given
        final String url = "/api/v1/membership/{membershipId}";
        final Long membershipId = 1L;
        // when

        ResultActions resultActions = mockMvc.perform(delete(url, membershipId));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("멤버십 삭제 성공")
    void remove_membership_success_test() throws Exception {
        // given
        final String url = "/api/v1/membership/{membershipId}";
        final Long membershipId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete(url, membershipId)
                .header(USER_ID_HEADER, userId));

        // then
        resultActions.andExpect(status().isOk());

    }



}