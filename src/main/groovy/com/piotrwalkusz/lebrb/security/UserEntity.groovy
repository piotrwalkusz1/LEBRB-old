package com.piotrwalkusz.lebrb.security

import com.piotrwalkusz.lebrb.common.BaseEntity
import com.piotrwalkusz.lebrb.common.Language

import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = 'user')
class UserEntity extends BaseEntity {

    String username
    String password
    Set<RoleEntity> roles
    Map<Language, Integer[]> knownWords

    @Column(unique = true)
    String getUsername() { username }

    @ManyToMany
    Set<RoleEntity> getRoles() { roles }

    @ElementCollection
    Map<Language, Integer[]> getKnownWords() { knownWords }
}
