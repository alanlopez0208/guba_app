package com.guba.app.domain.dto;

import com.guba.app.domain.models.Carrera;

public record AlumnoCarreraDTO(
        String matricula,
        Carrera carrera
){
}
