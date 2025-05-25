package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Find a user by their username.
     * @param username
     * @return
     */
    Optional<UserEntity> findByUsername(String username);
}
