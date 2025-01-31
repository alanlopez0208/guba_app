package com.guba.app.data.dao;

import com.guba.app.data.local.database.conexion.Conexion;
import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Carrera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public class DAOCarreras {

    Connection conn;

    private DataConsumer<Carrera> dataConsumer;

    public DAOCarreras() {
        dataConsumer = new DataConsumer<>();
    }


    public List<Carrera> getAllCarreras() {
        String sentencia = "SELECT * FROM Carreras";
        return dataConsumer.getList(sentencia, this::mapResultSetToCarrera);

    }

    public Optional<Integer> insertCarrera(Carrera carrera) {
        String sql = "INSERT INTO Carreras (Clave ,Nombre, HBCA, HTI, Creditos, TotalHoras, Modalidad, TotalAsignaturas ) VALUES (?,?,?,?,?,?,?,?)";

        return dataConsumer.executeUpdateWithGeneratedKeys(sql, t -> {
            t.setString(1, carrera.getIdClave());
            t.setString(2, carrera.getNombre());
            t.setString(3, carrera.getHbca());
            t.setString(4, carrera.getHti());
            t.setString(5, carrera.getCreditos());
            t.setString(6, carrera.getTotalHoras());
            t.setString(7, carrera.getModalidad());
            t.setString(8, carrera.getTotalAsignaturas());
        });
    }


    public boolean updateCarrera(Carrera carrera) {
        String sentencia = "UPDATE Carreras SET Clave = ?,Nombre = ?, HBCA = ?, HTI = ?, Creditos = ?, TotalHoras = ?, Modalidad = ?, TotalAsignaturas = ? WHERE IdCarrera = ?;";
        return dataConsumer.executeUpdate(sentencia, pstmt -> {
            pstmt.setString(1, carrera.getIdClave());
            pstmt.setString(2, carrera.getNombre());
            pstmt.setString(3, carrera.getHbca());
            pstmt.setString(4, carrera.getHti());
            pstmt.setString(5, carrera.getCreditos());
            pstmt.setString(6, carrera.getTotalHoras());
            pstmt.setString(7, carrera.getModalidad());
            pstmt.setString(8, carrera.getTotalAsignaturas());
            pstmt.setString(9, carrera.getIdCarrera());
        });
    }

    public boolean eliminarCarrera(String id) {
        String sql = "DELETE FROM Carreras WHERE IdCarrera = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, id);
        });
    }



    public Carrera getCarreraByd(String id) {
        Carrera Carrera = new Carrera();
        String sql = "SELECT * FROM Carreras WHERE IdCarrera = ?";
        return dataConsumer.getData(sql,pstmt->{
            pstmt.setString(1, id);
        }, this::mapResultSetToCarrera);
    }


    private Carrera mapResultSetToCarrera(ResultSet rs)  {
        try {
            Carrera carrera = new Carrera();
            carrera.setCreditos(rs.getString("Creditos"));
            carrera.setIdCarrera(rs.getString("IdCarrera"));
            carrera.setIdClave(rs.getString("Clave"));
            carrera.setNombre(rs.getString("Nombre"));
            carrera.setHbca(rs.getString("HBCA"));
            carrera.setHti(rs.getString("HTI"));
            carrera.setCreditos(rs.getString("Creditos"));
            carrera.setTotalHoras(rs.getString("TotalHoras"));
            carrera.setModalidad(rs.getString("Modalidad"));
            carrera.setTotalAsignaturas(rs.getString("TotalAsignaturas"));
            return carrera;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Carrera> buscarCarreras(String where, String filtro) {
        ArrayList<Carrera> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Carreras WHERE " + where + " LIKE ?";

        conn = Conexion.getConection();

        if (conn == null) {
            throw new RuntimeException("No se pudo conectar a la base de datos");
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, filtro + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Carrera carrera = mapResultSetToCarrera(rs);
                    resultados.add(carrera);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar las Carrera ", e);
        }
        return resultados;
    }
}
