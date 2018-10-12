package br.ufal.ic.academico.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class DisciplineDTO {
    private String code;
    private String name;
    private int credits;
    private int prerequisiteCredits;
    private List<String> prerequisiteDisciplinesCode;
    private String professor;
    private List<StudentDTO> enrolledStudents;

    public DisciplineDTO(String code, String name, int credits, int prerequisiteCredits, List<String> prerequisiteDisciplinesCode) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.prerequisiteCredits = prerequisiteCredits;
        this.prerequisiteDisciplinesCode = prerequisiteDisciplinesCode;
        this.professor = null;
        this.enrolledStudents = null;
    }
}
