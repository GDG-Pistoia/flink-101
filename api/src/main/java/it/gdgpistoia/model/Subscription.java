package it.gdgpistoia.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Subscription extends BaseEntity {
    String cmd;
}
