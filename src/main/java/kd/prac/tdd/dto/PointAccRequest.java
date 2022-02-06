package kd.prac.tdd.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PointAccRequest {

    @NotNull
    private Long membershipId;

    @Min(0)
    private Integer price;

    @Builder
    public PointAccRequest(Long membershipId, Integer price) {
        this.membershipId = membershipId;
        this.price = price;
    }
}
