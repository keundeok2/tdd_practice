package kd.prac.tdd.test;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Test {

    @Id @GeneratedValue
    private Long id;

    private String name;

    public Test(String name) {
        this.name = name;
    }
}
