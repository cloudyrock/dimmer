package com.github.cloudyrock.dimmer.samples.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryITest {


    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddUser() {
        // given
        User user = new User();
        final String user1 = "USER1";
        user.setName(user1);
        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByName(user1);

        // then
        assertThat(found.getName()).isEqualTo(found.getName());
    }

}
