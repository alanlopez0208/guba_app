package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.data.local.database.SQLTransactionalOperation;
import com.guba.app.domain.models.Periodo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


public class DAOPeriodo {

    private DataConsumer<Periodo> dataConsumer;

    public DAOPeriodo(){
        this.dataConsumer = new DataConsumer<>();
    }

    public Optional<Boolean> crearPeriodo(Periodo periodo) {
        String sqlPeriodo = "INSERT INTO Periodo (Nombre, Inicio, Fin) VALUES (?, ?, ?)";
        String sqlActualizarSemestre = "UPDATE Alumnos SET Semestre = CASE "
                + "WHEN Semestre < 8 THEN Semestre + 1 "
                + "ELSE Semestre "
                + "END";
        String sqlEliminarGruposMaterias = "DELETE FROM GruposMaterias";
        String sqlEliminarGrupos = "DELETE FROM Grupos";

        return dataConsumer.executeTransaction((SQLTransactionalOperation<Boolean>) connection -> {
            PreparedStatement pstmtPeriodo = connection.prepareStatement(sqlPeriodo);
            pstmtPeriodo.setString(1, periodo.getNombre());
            pstmtPeriodo.setString(2, periodo.getInicio());
            pstmtPeriodo.setString(3, periodo.getFin());
            int affectedRowsPeriodo = pstmtPeriodo.executeUpdate();


            PreparedStatement pstmtActualizarSemestre = connection.prepareStatement(sqlActualizarSemestre);
            int affectedRowsSemestre = pstmtActualizarSemestre.executeUpdate();

            PreparedStatement pstmtEliminarGruposMaterias = connection.prepareStatement(sqlEliminarGruposMaterias);
            int affectedRowsGruposMaterias = pstmtEliminarGruposMaterias.executeUpdate();

            PreparedStatement pstEliminarGrupos = connection.prepareStatement(sqlEliminarGrupos);
            int affectedRowsGrupos = pstEliminarGrupos.executeUpdate();

            if (affectedRowsPeriodo > 0 && affectedRowsSemestre >= 0 && affectedRowsGruposMaterias >= 0 && affectedRowsGrupos >= 0) {
               return true;
            } else {
                throw new SQLException("Error al crear perido");
            }
        });
    }

    public Periodo getPeriodoById(String id) {
        String sql = "SELECT * FROM Periodo WHERE IdPeriodo = ?";
        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, id);
        },this::mapResultSetToPeriodo);
    }

    public Periodo getUltimoPeriodo() {
        String sql = "SELECT * FROM Periodo ORDER BY IdPeriodo DESC LIMIT 1";
        return dataConsumer.getData(sql, this::mapResultSetToPeriodo);
    }

    private Periodo mapResultSetToPeriodo(ResultSet rs){
        try {
            Periodo periodo = new Periodo();
            periodo.setId(rs.getString("IdPeriodo"));
            periodo.setNombre(rs.getString("Nombre"));
            periodo.setInicio(rs.getString("Inicio"));
            periodo.setFin(rs.getString("Fin"));
            return periodo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
