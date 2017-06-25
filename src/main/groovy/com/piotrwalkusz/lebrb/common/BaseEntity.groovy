package com.piotrwalkusz.lebrb.common

import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
class BaseEntity {

    Long id

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long getId() { id }
}
