package kd.prac.tdd.Entity;

import kd.prac.tdd.enums.MembershipType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Membership {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MembershipType membershipName;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer point;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder
    public Membership(MembershipType membershipName, String userId, Integer point) {
        this.membershipName = membershipName;
        this.userId = userId;
        this.point = point;
    }
}
