package br.ufal.ic.academico.models;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

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

    private String name;

    private int credits, prerequisiteCredits;

    private List<Discipline> prerequisiteDisciplines;

    public Discipline(String code, String name, int credits, int prerequisiteCredits, List<Discipline> prerequisiteDisciplines) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.prerequisiteCredits = prerequisiteCredits;
        this.prerequisiteDisciplines = prerequisiteDisciplines;
    }
}
