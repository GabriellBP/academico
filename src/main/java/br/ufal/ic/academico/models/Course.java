package br.ufal.ic.academico.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private List<Discipline> discRequired;
    private List<Discipline> discElective;

    public Course(List<Discipline> discRequired, List<Discipline> discElective) {
        this.discRequired = discRequired;
        this.discElective = discElective;
    }
}
