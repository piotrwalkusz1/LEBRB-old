package com.piotrwalkusz.lebrb.security

import com.piotrwalkusz.lebrb.common.BaseEntity

import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.Table

@Table(name = 'role')
@Entity
class RoleEntity extends BaseEntity {

    String name
    Set<UserEntity> users

    @ManyToMany(mappedBy = 'roles')
    Set<UserEntity> getUsers() { users }
}
