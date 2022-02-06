package kd.prac.tdd.service;

import org.springframework.stereotype.Service;

@Service
public class RatedPointService implements PointService{

    private static final double RATIO = 0.01;

    @Override
    public int calcultate(int price) {
        return (int) (price * RATIO);
    }
}
