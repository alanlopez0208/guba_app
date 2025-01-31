package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Acuerdo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOAcuerdo {
    private final DataConsumer<Acuerdo> dataConsumer = new DataConsumer<>();

    private Acuerdo mapResultSetToAcuerdo(ResultSet result) {
        Acuerdo modelo = new Acuerdo();
        try {
            modelo.setNumero(result.getString("Acuerdo"));
            modelo.setFecha(result.getString("Fecha"));
            modelo.setCc(result.getString("CC"));
            return modelo;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Acuerdo getAcuerdo() {
        String sql = "SELECT * FROM Acuerdo ORDER BY IdAcuerdo DESC LIMIT 1";
        return dataConsumer.getData(sql,this::mapResultSetToAcuerdo);
    }

    public boolean actualizarAcuerdo(Acuerdo acuerdo) {
        String sql = "INSERT INTO Acuerdo (Acuerdo, Fecha, CC ) VALUES (?,?,?)";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, acuerdo.getNumero());
            pstmt.setString(2, acuerdo.toStringDate());
            pstmt.setString(3, acuerdo.getCc());
        });
    }
}
