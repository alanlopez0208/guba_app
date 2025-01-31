package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Grupo;
import com.guba.app.domain.models.GrupoMateria;
import com.guba.app.domain.models.Maestro;
import com.guba.app.domain.models.Materia;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class DAOGrupoMateria {

    private DataConsumer<GrupoMateria> dataConsumer;

    final String SELECT_ALL = """
                                SELECT
                                gm.Cursada AS GrupoMateria_Cursada,
                                gm.IdGrupoMateria AS GrupoMateria_Id,
                                gm.Prioridad AS GrupoMateria_Prioridad,
                                g.IdGrupo AS Grupo_IdGrupo,
                                g.Nombre AS Grupo_Nombre,
                                g.Semestre AS Grupo_Semestre,
                                g.IdCarrera AS Grupo_IdCarrera,
                                d.IdDocente AS Docente_IdDocente,
                                d.Nombre AS Docente_Nombre,
                                d.ApellidoPaterno AS Docente_ApellidoPaterno,
                                d.ApellidoMaterno AS Docente_ApellidoMaterno,
                                m.IdMateria AS Materia_IdMateria,
                                m.Nombre AS Materia_Nombre,
                                m.Semestre AS Materia_Semestre,
                                m.Clave AS Materia_Clave,
                                m.Modalidad AS Materia_Modalidad
                            FROM
                                GruposMaterias as gm
                                LEFT JOIN Grupos as g ON gm.IdGrupo = g.IdGrupo
                                LEFT JOIN Docentes as d ON gm.IdDocente = d.IdDocente
                                LEFT JOIN Materias as m ON gm.IdMateria = m.IdMateria
                                """;

    public DAOGrupoMateria(){
        dataConsumer = new DataConsumer<>();
    }

    private GrupoMateria mapResultSetToGrupoMateria(ResultSet rs){
        try {
            Materia materia = new Materia();
            materia.setIdMateria(rs.getString("Materia_IdMateria"));
            materia.setNombre(rs.getString("Materia_Nombre"));
            materia.setSemestre(rs.getString("Materia_Semestre"));
            materia.setClave(rs.getString("Materia_Clave"));
            materia.setModalidad(rs.getString("Materia_Modalidad"));

            Maestro maestro = new Maestro();
            maestro.setId(rs.getString("Docente_IdDocente"));
            maestro.setNombre(rs.getString("Docente_Nombre"));
            maestro.setApPat(rs.getString("Docente_ApellidoPaterno"));
            maestro.setApMat(rs.getString("Docente_ApellidoMaterno"));

            GrupoMateria grupoMateria = new GrupoMateria();
            grupoMateria.setIdGrupoMateria(rs.getString("GrupoMateria_Id"));
            grupoMateria.setIdGrupo(rs.getString("Grupo_IdGrupo"));
            grupoMateria.setMateria(materia);
            grupoMateria.setMaestro(maestro);
            grupoMateria.setPrioridad(rs.getInt("GrupoMateria_Prioridad"));
            grupoMateria.setCursada(rs.getInt("GrupoMateria_Cursada"));
            return grupoMateria;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<GrupoMateria> getGruposMaterias() {
       return dataConsumer.getList(SELECT_ALL, this::mapResultSetToGrupoMateria);
    }

    public boolean agregarGrupoMateria(GrupoMateria grupoMateria){
        String sql = "INSERT INTO GruposMaterias (IdGrupo, IdMateria, IdDocente, Cursada, Prioridad) VALUES (?, ?, ?, ?,?)";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, grupoMateria.getIdGrupo());
            pstmt.setString(2, grupoMateria.getMateria().getIdMateria());
            pstmt.setString(3, grupoMateria.getMaestro().getId());
            pstmt.setString(4, "0");
        });
    }

    public boolean agregarMaterias(List<Materia> materias, String idGrupo ){
        String sql = "INSERT INTO GruposMaterias (IdGrupo, IdMateria, Cursada) VALUES (?, ?, 0)";
        return dataConsumer.excuteBatch(sql, pstmt->{
            for (Materia materia : materias) {
                pstmt.setString(1, idGrupo);
                pstmt.setString(2, materia.getIdMateria());
                pstmt.addBatch();
            }
        });
    }

    public boolean agregarGrupoMaterias(Grupo grupo) {
        String sqlMaterias = """
                INSERT INTO GruposMaterias (IdGrupo, IdMateria)
                SELECT ?, IdMateria
                FROM Materias
                WHERE IdCarrera = ? AND Semestre = ?""";
        return dataConsumer.executeUpdate(sqlMaterias, preparedStatement -> {
                preparedStatement.setString(1,grupo.getId());
                preparedStatement.setString(2,grupo.getCarrera().getIdCarrera());
                preparedStatement.setString(3, grupo.getSemestre());
        });
    }

    public boolean eliminarGrupoMateria(String idGrupoMateria) {
        String sql = "DELETE FROM GruposMaterias WHERE IdGrupoMateria = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, idGrupoMateria);
        });
    }

    public boolean eliminimarByIdGrupo(String idGrupo){
        String sql = "DELETE FROM GruposMaterias WHERE IdGrupo = ?";
        return dataConsumer.executeUpdate(sql,pstmt->{
            pstmt.setString(1, idGrupo);
        });
    }

    public boolean actualizarGrupoMateria(GrupoMateria grupoMateria) {
        String sql = "UPDATE GruposMaterias SET IdGrupo = ?, IdMateria = ?, IdDocente = ?, Cursada = ? WHERE IdGrupoMateria = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, grupoMateria.getIdGrupo());
            pstmt.setString(2, grupoMateria.getMateria().getIdMateria());
            pstmt.setString(3, grupoMateria.getMaestro().getId());
            pstmt.setInt(4, grupoMateria.getCursada());
            pstmt.setString(5, grupoMateria.getIdGrupoMateria());
        });
    }



    public GrupoMateria obtenerGrupoMateriaPorId(int idGrupoMateria) {
        GrupoMateria grupoMateria = null;
        String sql = SELECT_ALL+" WHERE IdGrupoMateria = ?";

        return  dataConsumer.getData(sql, pstmt-> {
            pstmt.setInt(1, idGrupoMateria);
        },this::mapResultSetToGrupoMateria);
    }


    public List<GrupoMateria> obtenerGruposMateriasPorIdGrupo(String idGrupo) {
        String sql = SELECT_ALL+ " WHERE gm.IdGrupo = ?";
        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, idGrupo);
        }, this::mapResultSetToGrupoMateria);
    }

    public Optional<Boolean> actualizarIdDocente(GrupoMateria grupoMateria, String nuevoIdDocente) {
        return dataConsumer.executeTransaction(connection -> {
            String sql = "UPDATE GruposMaterias SET IdDocente = ?, Prioridad = (SELECT (count(*) +1) FROM GruposMaterias WHERE IdDocente = ? AND IdGrupo = ?) WHERE IdGrupoMateria = ?";
            String updateCalificaciones = "UPDATE Calificaciones SET IdDocente = ? WHERE IdMateria = ? AND IdPeriodo = (SELECT IdPeriodo FROM Periodo ORDER BY IdPeriodo DESC LIMIT 1)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nuevoIdDocente);
                pstmt.setString(2, nuevoIdDocente);
                pstmt.setString(3, grupoMateria.getIdGrupo());
                pstmt.setString(4,grupoMateria.getIdGrupoMateria());
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected == 0) {
                    throw new SQLException("Hubo un error al actualizar el profesor");
                }

                try (PreparedStatement pstmtCalificaciones = connection.prepareStatement(updateCalificaciones)) {
                    pstmtCalificaciones.setString(1, nuevoIdDocente);
                    pstmtCalificaciones.setString(2, grupoMateria.getMateria().getIdMateria());
                    rowsAffected = pstmtCalificaciones.executeUpdate();

                    System.out.println(rowsAffected);
                    if (rowsAffected < 0) {
                        throw new SQLException("Hubo un error al actualizar el profesor en las calificaciones");
                    }
                }
            }
            return true;
        });
    }

}
