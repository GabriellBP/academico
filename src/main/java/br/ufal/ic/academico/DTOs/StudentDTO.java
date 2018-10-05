package br.ufal.ic.academico.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class StudentDTO {
    private Long id;
    private String name;
}
