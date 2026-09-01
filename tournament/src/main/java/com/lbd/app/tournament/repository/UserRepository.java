package com.lbd.app.tournament.repository;

import com.lbd.app.tournament.model.User;
import com.lbd.app.tournament.repository.query.UserQueries;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph("User.role")
    Optional<User> findByEmail(String username);

    /**
     * @return 1 if exists users.
     */
    @Query(nativeQuery = true,
            value = UserQueries.EXISTS_USER)
    Integer existUsers();


    @EntityGraph("User.role")
    List<User> findAll();
}
