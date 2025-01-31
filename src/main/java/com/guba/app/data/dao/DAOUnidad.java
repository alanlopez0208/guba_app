package com.guba.app.data.dao;

import com.guba.app.domain.models.Unidad;
import com.guba.app.data.local.database.DataConsumer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOUnidad {
    private final DataConsumer<Unidad> dataConsumer;

    public DAOUnidad() {
        this.dataConsumer = new DataConsumer<>();
    }


    public Optional<Integer> insertarUnidad(Unidad unidad) {
        String sql = "INSERT INTO Unidades (IdCurso, Nombre, HTI, HCBA) VALUES (?, ?, ?, ?)";
        return dataConsumer.executeUpdateWithGeneratedKeys(sql, pstmt -> {
            pstmt.setInt(1, unidad.getIdCurso());
            pstmt.setString(2, unidad.getNombre());
            pstmt.setInt(3, unidad.getHti());
            pstmt.setInt(4, unidad.getHcba());
        });
    }


    public List<Unidad> obtenerTodasLasUnidades() {
        String sql = "SELECT * FROM Unidades";
        return dataConsumer.getList(sql, this::convertirUnidad);
    }


    public List<Unidad> obtenerUnidadesPorCurso(int idCurso) {
        String sql = "SELECT * FROM Unidades WHERE IdCurso = ?";
        return dataConsumer.getList(sql, pstmt -> pstmt.setInt(1, idCurso), this::convertirUnidad);
    }

    public boolean actualizarUnidad(Unidad unidad) {
        String sql = "UPDATE Unidades SET Nombre = ?, HTI = ?, HCBA = ? WHERE IdUnidad = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, unidad.getNombre());
            pstmt.setInt(2, unidad.getHti());
            pstmt.setInt(3, unidad.getHcba());
            pstmt.setInt(4, unidad.getIdUnidad());
        });
    }

    public boolean eliminarUnidad(int idUnidad) {
        String sql = "DELETE FROM Unidades WHERE IdUnidad = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> pstmt.setInt(1, idUnidad));
    }

    private Unidad convertirUnidad(ResultSet rs) {
        Unidad unidad = new Unidad();
        try {
            unidad.setIdUnidad(rs.getInt("IdUnidad"));
            unidad.setIdCurso(rs.getInt("IdCurso"));
            unidad.setNombre(rs.getString("Nombre"));
            unidad.setHti(rs.getInt("HTI"));
            unidad.setHcba(rs.getInt("HCBA"));
            return unidad;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
