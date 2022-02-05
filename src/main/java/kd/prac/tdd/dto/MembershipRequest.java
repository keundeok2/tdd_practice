package kd.prac.tdd.dto;

import kd.prac.tdd.enums.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MembershipRequest {

    @NotNull
    private MembershipType membershipType;

    @NotNull
    @Min(0)
    private Integer point;
}
