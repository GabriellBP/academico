package br.ufal.ic.academico.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    private Secretary postGraduateSecretary;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    private Secretary underGraduateSecretary;
}
