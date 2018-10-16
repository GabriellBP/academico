package br.ufal.ic.academico.models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String code;

    @Setter
    private String name;

    private int credits, prerequisiteCredits;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Discipline> prerequisiteDisciplines;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Student> enrolledStudents;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Professor professor;

    public Discipline(String code, String name, int credits, int prerequisiteCredits, List<Discipline> prerequisiteDisciplines, Professor professor) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.prerequisiteCredits = prerequisiteCredits;
        this.prerequisiteDisciplines = prerequisiteDisciplines;
        this.enrolledStudents = new LinkedList<>();
        this.professor = professor;
    }

    public Discipline(String code, String name, int credits, int prerequisiteCredits, Professor professor) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.prerequisiteCredits = prerequisiteCredits;
        this.prerequisiteDisciplines = null;
        this.enrolledStudents = new LinkedList<>();
        this.professor = professor;
    }

    public Discipline(String code, String name, int credits, int prerequisiteCredits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.prerequisiteCredits = prerequisiteCredits;
        this.prerequisiteDisciplines = null;
        this.enrolledStudents = new LinkedList<>();
        this.professor = null;
    }

    public void enrollStudent(Student student) {
        this.enrolledStudents.add(student);
    }
}
