package com.guba.app.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlumnoDto (
    String IdAlumno,
    String Matricula,
    String Password
){

}
