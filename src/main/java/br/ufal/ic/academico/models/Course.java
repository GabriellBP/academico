package br.ufal.ic.academico.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Discipline> discRequired;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Discipline> discElective;

    public Course(List<Discipline> discRequired, List<Discipline> discElective) {
        this.discRequired = discRequired;
        this.discElective = discElective;
    }
}
