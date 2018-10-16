package br.ufal.ic.academico.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
public class Secretary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    private List<Course> courses;

    public Secretary(List<Course> courses) {
        this.courses = courses;
    }

    public Secretary() {
        this.courses = new LinkedList<>();
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
