package br.ufal.ic.academico.models;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @Column(unique = true)
    private String name;

    @Setter
    @ManyToOne(cascade = CascadeType.ALL)
    private Course course;

    @Setter
    private int credits;

    @Setter
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Discipline> finalizedDisciplines;

    public Student(String name, Course course) {
        this.name = name;
        this.credits = 0;
        this.course = course;
        this.finalizedDisciplines = new LinkedList<>();
    }

    public Student(String name) {
        this.name = name;
        this.credits = 0;
        this.course = null;
        this.finalizedDisciplines = new LinkedList<>();
    }

    public void addFinalizedDisciplines(Discipline discipline) {
        this.credits += discipline.getCredits();
        this.finalizedDisciplines.add(discipline);
    }

    public boolean enrollmentCourse(Course course) {
        if (course == null) {
            this.course = course;
            return true;
        } else {
            return false;
        }
    }
}
