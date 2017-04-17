package org.trivago.light.application.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;
}
