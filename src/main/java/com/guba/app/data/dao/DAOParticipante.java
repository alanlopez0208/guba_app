package com.guba.app.data.dao;

import com.guba.app.domain.models.Participante;
import com.guba.app.data.local.database.DataConsumer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOParticipante {
    private final DataConsumer<Participante> dataConsumer;

    public DAOParticipante() {
        this.dataConsumer = new DataConsumer<>();
    }

    public Optional<Integer> insertarParticipante(Participante participante) {
        String sql = "INSERT INTO Participantes (IdCurso, Nombre ) VALUES (?, ?)";
        return dataConsumer.executeUpdateWithGeneratedKeys(sql, pstmt -> {
            pstmt.setInt(1, participante.getIdCurso());
            pstmt.setString(2, participante.getNombre());
        });
    }

    public List<Participante> obtenerTodosLosParticipantes() {
        String sql = "SELECT * FROM Participantes";
        return dataConsumer.getList(sql, this::convertirParticipante);
    }
    public List<Participante> obtenerParticipantesPorCurso(int idCurso) {
        String sql = "SELECT * FROM Participantes WHERE IdCurso = ?";
        return dataConsumer.getList(sql, pstmt -> pstmt.setInt(1, idCurso), this::convertirParticipante);
    }

    public boolean actualizarParticipante(Participante participante) {
        String sql = "UPDATE Participantes SET Nombre = ? WHERE IdParticipante = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, participante.getNombre());
            pstmt.setInt(2, participante.getIdParticipante());
        });
    }

    // Eliminar participante
    public boolean eliminarParticipante(int idParticipante) {
        String sql = "DELETE FROM Participantes WHERE IdParticipante = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> pstmt.setInt(1, idParticipante));
    }

    // Método privado para convertir ResultSet a Participante
    private Participante convertirParticipante(ResultSet rs)  {
        Participante participante = new Participante();
        try {
            participante.setIdParticipante(rs.getInt("IdParticipante"));
            participante.setIdCurso(rs.getInt("IdCurso"));
            participante.setNombre(rs.getString("Nombre"));
            return participante;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
