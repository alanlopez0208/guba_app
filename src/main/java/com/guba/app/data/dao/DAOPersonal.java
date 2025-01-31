package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Personal;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOPersonal {
    private DataConsumer<Personal> dataConsumer = new DataConsumer<>();

    public List<Personal> getPersonales() {
        String sql = "SELECT * FROM Personal";
        return dataConsumer.getList(sql,this::mapResultSetToPersonal);
    }
    
    public List<Personal> buscarPersonales(String where, String filtro) {
        String sql = "SELECT * FROM Personal WHERE " + where + " LIKE ? ";

        return dataConsumer.getList(sql,this::mapResultSetToPersonal);
    }
    

    
    public boolean updatePersonal(Personal Personal) {
        String sql = "UPDATE Personal SET CURP = ?, Nombre = ?, ApellidoPaterno = ?, ApellidoMaterno = ?, Genero = ?, "
                + "CorreoPersonal = ?, CorreoInstitucional = ?, Domicilio = ?, Celular = ?, Estado = ?, Municipio = ?, Matricula = ?, Password = ?, Foto = ?, RFC = ?"
                + " WHERE IdPersonal = ?";

        return  dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, Personal.getCurp());
            pstmt.setString(2, Personal.getNombre());
            pstmt.setString(3, Personal.getApPat());
            pstmt.setString(4, Personal.getApMat());
            pstmt.setString(5, Personal.getGenero());
            pstmt.setString(6, Personal.getCorreoPer());
            pstmt.setString(7, Personal.getCorreoIns());
            pstmt.setString(8, Personal.getDomicilio());
            pstmt.setString(9, Personal.getCelular());
            pstmt.setString(10, Personal.getEstado());
            pstmt.setString(11, Personal.getMunicipio());
            pstmt.setString(12, Personal.getMatricula());
            pstmt.setString(13, Personal.getPassword());
            pstmt.setString(14, Personal.getFoto());
            pstmt.setString(15, Personal.getRfc());
            pstmt.setString(16, Personal.getId());
        });
    }


    public boolean deletePersonal(String id) {
        String sql = "DELETE FROM Personal WHERE IdPersonal = ?";

        return dataConsumer.executeUpdate(sql,pstmt->{
            pstmt.setString(1, id);
        });
    }


    public Personal retornarPersonal(String rfc) {
        String sql = "SELECT  * WHERE RFC = ?";
        Personal personal = null;

        return  dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, rfc);
        },this::mapResultSetToPersonal);
    }


    public Optional<Integer> crearPersonal(Personal Personal) {
        String sql = "INSERT INTO Personal (RFC, CURP, Nombre, ApellidoPaterno, ApellidoMaterno, Genero, CorreoPersonal, "
                + "CorreoInstitucional, Domicilio, Celular, Estado, Municipio, Matricula, Password, Foto )"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        return dataConsumer.executeUpdateWithGeneratedKeys(sql, pstmt->{
            pstmt.setString(1, Personal.getRfc());
            pstmt.setString(2, Personal.getCurp());
            pstmt.setString(3, Personal.getNombre());
            pstmt.setString(4, Personal.getApPat());
            pstmt.setString(5, Personal.getApMat());
            pstmt.setString(6, Personal.getGenero());
            pstmt.setString(7, Personal.getCorreoPer());
            pstmt.setString(8, Personal.getCorreoIns());
            pstmt.setString(9, Personal.getDomicilio());
            pstmt.setString(10, Personal.getCelular());
            pstmt.setString(11, Personal.getEstado());
            pstmt.setString(12, Personal.getMunicipio());
            pstmt.setString(13, Personal.getMatricula());
            pstmt.setString(14, Personal.getRfc());
            pstmt.setString(15, Personal.getFoto());
        });
    }


    private Personal mapResultSetToPersonal(ResultSet rs){
        try {
            Personal personal = new Personal();
            personal.setId(rs.getString("IdPersonal"));
            personal.setRfc(rs.getString("RFC"));
            personal.setCurp(rs.getString("CURP"));
            personal.setNombre(rs.getString("Nombre"));
            personal.setApPat(rs.getString("ApellidoPaterno"));
            personal.setApMat(rs.getString("ApellidoMaterno"));
            personal.setGenero(rs.getString("Genero"));
            personal.setCorreoPer(rs.getString("CorreoPersonal"));
            personal.setCorreoIns(rs.getString("CorreoInstitucional"));
            personal.setDomicilio(rs.getString("Domicilio"));
            personal.setCelular(rs.getString("Celular"));
            personal.setEstado(rs.getString("Estado"));
            personal.setMunicipio(rs.getString("Municipio"));
            personal.setMatricula(rs.getString("Matricula"));
            personal.setPassword(rs.getString("Password"));
            personal.setFoto(rs.getString("Foto"));
            return personal;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
