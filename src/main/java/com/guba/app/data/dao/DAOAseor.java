package com.guba.app.data.dao;

import com.guba.app.domain.models.Asesor;
import com.guba.app.data.local.database.DataConsumer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOAseor {
    private final DataConsumer<Asesor> dataConsumer;

    public DAOAseor() {
        this.dataConsumer = new DataConsumer<>();
    }


    public Optional<Integer> insertarAsesor(Asesor asesor) {
        String sql = "INSERT INTO Asesores (IdCurso, Nombre, Lugar, Puesto) VALUES (?, ?, ?, ?)";
        return dataConsumer.executeUpdateWithGeneratedKeys(sql, pstmt -> {
            pstmt.setInt(1, asesor.getIdCurso());
            pstmt.setString(2, asesor.getNombre());
            pstmt.setString(3, asesor.getLugar());
            pstmt.setString(5, asesor.getPuesto());
        });
    }


    public Asesor obtenerAsesorPorCurso(int idCurso) {
        String sql = "SELECT * FROM Asesores WHERE IdCurso = ?";
        return dataConsumer.getData(sql, pstmt -> pstmt.setInt(1, idCurso), this::convertirAsesor);
    }

    public List<Asesor> obtenerTodosLosAsesores() {
        String sql = "SELECT * FROM Asesores";
        return dataConsumer.getList(sql, this::convertirAsesor);
    }


    public List<Asesor> obtenerAsesoresPorCurso(int idCurso) {
        String sql = "SELECT * FROM Asesores WHERE IdCurso = ?";
        return dataConsumer.getList(sql, pstmt -> pstmt.setInt(1, idCurso), this::convertirAsesor);
    }

    public boolean actualizarAsesor(Asesor asesor) {
        String sql = "UPDATE Asesores SET Nombre = ?, Puesto = ?, Lugar = ? WHERE IdDocente = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, asesor.getNombre());
            pstmt.setString(2, asesor.getPuesto());
            pstmt.setString(3,asesor.getLugar());
            pstmt.setInt(4, asesor.getIdDocente());
        });
    }

    // Eliminar asesor
    public boolean eliminarAsesor(int idDocente) {
        String sql = "DELETE FROM Asesores WHERE IdDocente = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> pstmt.setInt(1, idDocente));
    }


    private Asesor convertirAsesor(ResultSet rs){
        Asesor asesor = new Asesor();
        try {
            asesor.setIdDocente(rs.getInt("IdDocente"));
            asesor.setIdCurso(rs.getInt("IdCurso"));
            asesor.setNombre(rs.getString("Nombre"));
            asesor.setLugar(rs.getString("Lugar"));
            asesor.setPuesto(rs.getString("Puesto"));
            return asesor;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
