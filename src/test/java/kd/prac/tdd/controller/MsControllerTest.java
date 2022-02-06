package kd.prac.tdd.controller;

import com.google.gson.Gson;
import kd.prac.tdd.enums.MembershipErrorResult;
import kd.prac.tdd.dto.MembershipRequest;
import kd.prac.tdd.enums.MembershipType;
import kd.prac.tdd.exception.MembershipException;
import kd.prac.tdd.service.MembershipService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest를 이용한 테스트
 * SpringWebMVC에 관한 Bean을 로드하여 사용할 수 있다.
 */
@WebMvcTest(MembershipController.class)
public class MsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private MembershipService membershipService;

    @Test
    void meckmvc_is_not_null() {
        Assertions.assertThat(mockMvc).isNotNull();
    }

    Gson gson = new Gson();


    @Test
    @DisplayName("서비스에서 예외발생하여 실패 테스트")
    void exception_occured_fail_test() throws Exception {
        // given
        final String url = "/api/v1/membership";
        final MembershipRequest request = new MembershipRequest(MembershipType.NAVER, 100);
//        BDDMockito.given(membershipService.addMembership("123", MembershipType.NAVER, 100)).willThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));
        BDDMockito.given(membershipService.addMembership(any(String.class), any(MembershipType.class), any(Integer.class))).willThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));
        // when
        ResultActions result = mockMvc.perform(post(url)
                .header("X-USER-ID", "123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(request)))
                .andDo(print());
        // then
        result.andExpect(status().isBadRequest());
    }
}
