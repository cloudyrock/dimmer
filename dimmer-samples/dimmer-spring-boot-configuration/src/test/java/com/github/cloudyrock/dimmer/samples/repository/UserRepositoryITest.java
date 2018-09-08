package com.github.cloudyrock.dimmer.samples.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManagerAutoConfiguration;
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
        UserEntity userEntity = new UserEntity();
        final String user1 = "USER1";
        userEntity.setName(user1);
        entityManager.persist(userEntity);
        entityManager.flush();

        // when
        UserEntity found = userRepository.findByName(user1);

        // then
        assertThat(found.getName()).isEqualTo(found.getName());
    }

}
