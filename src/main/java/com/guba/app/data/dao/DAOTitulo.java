package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Titulo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOTitulo {

    private final DataConsumer<Titulo> dataConsumer = new DataConsumer<>();


    public Titulo getTitulacionByIdAlumno(String idAlumno) {
        String sql = "SELECT * FROM Titulacion WHERE IdAlumno = ? LIMIT 1 ";
        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1,idAlumno);
        },this::mapResultSetToTitulacionModelo);
    }


    public boolean updateTitulacionByIdAlumno(Titulo titulo) {
        String sql = "UPDATE Titulacion SET Nombre = ?, Registro = ?, Libro = ?, Foja = ?, Folio = ?, "
                + "Acta = ?, TipoExamen = ?, FechaAplicacion = ?, HoraAplicacion = ?, "
                + "Duracion = ?, HoraFinalizacion = ?, Presidente = ?, Secretario = ?, Vocal = ?, Nombre = ? "
                + "WHERE IdAlumno = ?";

        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, titulo.getNombre());
            pstmt.setString(2, titulo.getRegistro());
            pstmt.setString(3, titulo.getLibro());
            pstmt.setString(4, titulo.getFoja());
            pstmt.setString(5, titulo.getFolio());
            pstmt.setString(6, titulo.getActa());
            pstmt.setString(7, titulo.getTipoExamen());
            pstmt.setString(8, titulo.getFechaAplicacion());
            pstmt.setString(9, titulo.getHoraAplicacion());
            pstmt.setString(10, titulo.getDuracion());
            pstmt.setString(11, titulo.getHoraFinalizacion());
            pstmt.setString(12, titulo.getPresidente());
            pstmt.setString(13, titulo.getSecretario());
            pstmt.setString(14, titulo.getVocal());
            pstmt.setString(15, titulo.getNombre());
            pstmt.setString(16, titulo.getIdAlumno());
        });
    }


    public boolean deleteTitulacionByIdAlumno(String idAlumno) {
        String sql = "DELETE FROM Titulacion WHERE IdAlumno = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> pstmt.setString(1, idAlumno));
    }


    public boolean crearTitulacion(Titulo titulo) {
        String sql = "INSERT INTO Titulacion (IdAlumno, Registro, Libro, Foja, Folio, Acuerdo, TipoExamen, FechaAplicacion, "
                + "HoraAplicacion, Duracion, HoraFinalizacion, Presidente, Secretario, Vocal, Nombre, Acta) "
                + "VALUES (?, ?, ?, ?, ?, (SELECT IdAcuerdo FROM Acuerdo ORDER BY IdAcuerdo DESC LIMIT 1),?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, titulo.getIdAlumno());
            pstmt.setString(2, titulo.getRegistro());
            pstmt.setString(3, titulo.getLibro());
            pstmt.setString(4, titulo.getFoja());
            pstmt.setString(5, titulo.getFolio());
            pstmt.setString(6, titulo.getTipoExamen());
            pstmt.setString(7, titulo.getFechaAplicacion());
            pstmt.setString(8, titulo.getHoraAplicacion());
            pstmt.setString(9, titulo.getDuracion());
            pstmt.setString(10, titulo.getHoraFinalizacion());
            pstmt.setString(11, titulo.getPresidente());
            pstmt.setString(12, titulo.getSecretario());
            pstmt.setString(13, titulo.getVocal());
            pstmt.setString(14, titulo.getNombre());
            pstmt.setString(15,titulo.getActa());
        });
    }


    private Titulo mapResultSetToTitulacionModelo(ResultSet rs)  {
        try {
            Titulo titulacion = new Titulo();
            titulacion.setIdTitulacion(rs.getString("IdTitulacion"));
            titulacion.setIdAlumno(rs.getString("IdAlumno"));
            titulacion.setNombre(rs.getString("Nombre"));
            titulacion.setRegistro(rs.getString("Registro"));
            titulacion.setLibro(rs.getString("Libro"));
            titulacion.setFoja(rs.getString("Foja"));
            titulacion.setFolio(rs.getString("Folio"));
            titulacion.setAcuerdo(rs.getString("Acuerdo"));
            titulacion.setTipoExamen(rs.getString("TipoExamen"));
            titulacion.setFechaAplicacion(rs.getString("FechaAplicacion"));
            titulacion.setHoraAplicacion(rs.getString("HoraAplicacion"));
            titulacion.setDuracion(rs.getString("Duracion"));
            titulacion.setHoraFinalizacion(rs.getString("HoraFinalizacion"));
            titulacion.setPresidente(rs.getString("Presidente"));
            titulacion.setSecretario(rs.getString("Secretario"));
            titulacion.setVocal(rs.getString("Vocal"));
            titulacion.setActa(rs.getString("Acta"));
            return titulacion;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
