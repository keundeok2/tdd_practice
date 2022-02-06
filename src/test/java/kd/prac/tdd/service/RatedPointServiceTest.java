package kd.prac.tdd.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RatedPointServiceTest {

    @InjectMocks
    RatedPointService pointService;

    @Test
    @DisplayName("포인트 100원 적립 = 1")
    void 적립_테스트() {
        // given
        int price = 100;

        // when
        int point = pointService.calcultate(price);

        // then
        assertThat(point).isEqualTo(1);

    }

    @Test
    @DisplayName("포인트 20000원 적립 = 200")
    void 적립_테스트_20000원() {
        // given
        int price = 20000;

        // when
        int point = pointService.calcultate(price);

        // then
        assertThat(point).isEqualTo(200);
    }


}