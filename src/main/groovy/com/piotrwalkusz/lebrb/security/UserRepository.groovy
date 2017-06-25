package com.piotrwalkusz.lebrb.security

import com.piotrwalkusz.lebrb.common.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username)

    @Query('SELECT u FROM UserEntity u LEFT JOIN FETCH u.knownWords m WHERE u.username = ?1')
    UserEntity findByUsernameAndFetchKnownWords(String username)
}
