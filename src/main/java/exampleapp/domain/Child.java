package exampleapp.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "children")
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
}
