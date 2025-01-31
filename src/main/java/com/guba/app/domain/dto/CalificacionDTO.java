package com.guba.app.domain.dto;


import com.fasterxml.jackson.annotation.JsonAlias;

public record CalificacionDTO (
        @JsonAlias(value = "IdCalificacion")
        int idCalificacion,
        @JsonAlias(value = "IdAlumno")
        int idAlumno,
        @JsonAlias(value = "IdDocente")
        int idDocente,
        @JsonAlias(value = "IdPeriodo")
        int idPeriodo,
        @JsonAlias(value = "IdMateria")
        int idMateria,
        Float P1U1,
        Float P2U1,
        Float P3U1,
        Float P4U1,
        Float P1U2,
        Float P2U2,
        Float P3U2,
        Float P4U2,
        Float P1U3,
        Float P2U3,
        Float P3U3,
        Float P4U3,
        Float P1U4,
        Float P2U4,
        Float P3U4,
        Float P4U4,
        Float TB,
        String Fecha,
        String Tipo,
        String Folio
){
}
