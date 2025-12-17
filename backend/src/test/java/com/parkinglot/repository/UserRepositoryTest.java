package com.parkinglot.repository;

import com.parkinglot.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByEmail_andExistsByEmail() {
        User u = new User();
        u.setFirstName("First");
        u.setLastName("Last");
        u.setEmail("repouser@example.com");
        u.setPassword("pw");
        userRepository.save(u);

        assertThat(userRepository.findByEmail("repouser@example.com")).isPresent();
        assertThat(userRepository.existsByEmail("repouser@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nope@example.com")).isFalse();
    }
}
