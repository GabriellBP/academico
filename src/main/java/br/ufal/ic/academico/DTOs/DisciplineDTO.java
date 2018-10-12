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


}
