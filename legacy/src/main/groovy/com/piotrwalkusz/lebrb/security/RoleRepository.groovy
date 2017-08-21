package com.piotrwalkusz.lebrb.security

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository extends JpaRepository<RoleEntity, Long> {

}