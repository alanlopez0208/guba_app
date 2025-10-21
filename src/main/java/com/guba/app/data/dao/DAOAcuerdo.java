package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Acuerdo;
import com.guba.app.domain.models.Carrera;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DAOAcuerdo {
    private final DataConsumer<Acuerdo> dataConsumer = new DataConsumer<>();

    private Acuerdo mapResultSetToAcuerdo(ResultSet result) {
        Acuerdo modelo = new Acuerdo();
        try {
            modelo.setNumero(result.getString("Acuerdo"));
            modelo.setFecha(result.getString("Fecha"));
            modelo.setCc(result.getString("CC"));
            modelo.setId(result.getInt("IdAcuerdo"));
            Carrera carrera = new Carrera();
            carrera.setIdCarrera(result.getString("IdCarrera"));
            carrera.setNombre(result.getString("NombreCarrera"));
            modelo.setCarrera(carrera);
            return modelo;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ✅ CORREGIDO: Espacio después de SELECT
    private final String SELECT_ACUERDO_WITH_CARRERA = "SELECT " +
            "a.IdAcuerdo, " +
            "a.Acuerdo, " +
            "a.Fecha, " +
            "a.CC, " +
            "a.IdCarrera, " +
            "c.Clave, " +
            "c.Nombre AS NombreCarrera, " +
            "c.HBCA, " +
            "c.HTI, " +
            "c.Creditos, " +
            "c.TotalHoras, " +
            "c.Modalidad, " +
            "c.TotalAsignaturas " +
            "FROM Acuerdo AS a " +
            "INNER JOIN Carreras AS c ON a.IdCarrera = c.IdCarrera";

    public Acuerdo getAcuerdo(String idAcuerdo) {
        String sql = SELECT_ACUERDO_WITH_CARRERA + " WHERE a.IdAcuerdo = ?";
        return dataConsumer.getData(sql, pstm -> {
            pstm.setString(1, idAcuerdo);
        }, this::mapResultSetToAcuerdo);
    }

    public boolean actualizarAcuerdo(Acuerdo acuerdo) {
        String sql = "UPDATE Acuerdo SET Acuerdo = ?, Fecha = ?, CC = ?, IdCarrera = ? WHERE IdAcuerdo = ?";

        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, acuerdo.getNumero());
            pstmt.setString(2, acuerdo.toStringDate());
            pstmt.setString(3, acuerdo.getCc());
            pstmt.setString(4, acuerdo.getCarrera().getIdCarrera());
            pstmt.setInt(5, acuerdo.getId());
        });
    }

    public boolean insertarAcuerdo(Acuerdo acuerdo) {
        String sql = "INSERT INTO Acuerdo (Acuerdo, Fecha, CC, IdCarrera) VALUES (?, ?, ?, ?)";

        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, acuerdo.getNumero());
            pstmt.setString(2, acuerdo.toStringDate());
            pstmt.setString(3, acuerdo.getCc());
            pstmt.setInt(4, Integer.parseInt(acuerdo.getCarrera().getIdCarrera()));
        });
    }

    public List<Acuerdo> getAllAcuerdos() {
        return dataConsumer.getList(SELECT_ACUERDO_WITH_CARRERA, this::mapResultSetToAcuerdo);
    }

    public List<Acuerdo> getAcuerdosByCarrera(int idCarrera) {
        String sql = SELECT_ACUERDO_WITH_CARRERA + " WHERE a.IdCarrera = ?";
        return dataConsumer.getList(sql, pstm -> {
            pstm.setInt(1, idCarrera);
        }, this::mapResultSetToAcuerdo);
    }

    public boolean eliminarAcuerdo(int idAcuerdo) {
        String sql = "DELETE FROM Acuerdo WHERE IdAcuerdo = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setInt(1, idAcuerdo);
        });
    }
}
