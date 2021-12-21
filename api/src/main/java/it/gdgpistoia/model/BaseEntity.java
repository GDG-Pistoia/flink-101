package it.gdgpistoia.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public abstract class BaseEntity{
    protected String uuid = UUID.randomUUID().toString();
}
