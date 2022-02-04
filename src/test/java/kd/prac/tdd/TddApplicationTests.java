package kd.prac.tdd;

import kd.prac.tdd.test.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
@Commit
class TddApplicationTests {

    @Autowired
    TestRepository testRepository;

    @PersistenceContext
    EntityManager em;
    @Test
    void contextLoads() {
        testRepository.save(new kd.prac.tdd.test.Test("tester"));

        em.flush();
        em.clear();

        List<kd.prac.tdd.test.Test> result = testRepository.findAll();

    }

}
