package com.guba.app.dao;

import com.guba.app.service.local.database.DataConsumer;
import com.guba.app.models.Maestro;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOMaestro {

    private DataConsumer<Maestro> dataConsumer;

    public DAOMaestro(){
        dataConsumer = new DataConsumer<>();
    }


    public List<Maestro> getDocentes() {
        String sql = "SELECT * FROM Docentes";
        return dataConsumer.getList(sql, this::mapResultSetToDocente);
    }

    public List<Maestro> buscarDocentes(String where, String filtro) {
        String sql = "SELECT * FROM Docentes WHERE " + where + " LIKE ?";
        return dataConsumer.getList(sql, this::mapResultSetToDocente);
    }

    private Maestro mapResultSetToDocente(ResultSet rs) {
        try {
            Maestro maestro = new Maestro();
            maestro.setRfc(rs.getString("RFC"));
            maestro.setCurp(rs.getString("CURP"));
            maestro.setNombre(rs.getString("Nombre"));
            maestro.setApPat(rs.getString("ApellidoPaterno"));
            maestro.setApMat(rs.getString("ApellidoMaterno"));
            maestro.setGenero(rs.getString("Genero"));
            maestro.setCorreoPer(rs.getString("CorreoPersonal"));
            maestro.setCorreoIns(rs.getString("CorreoInstitucional"));
            maestro.setDomicilio(rs.getString("Domicilio"));
            maestro.setCelular(rs.getString("Celular"));
            maestro.setEstado(rs.getString("Estado"));
            maestro.setMunicipio(rs.getString("Municipio"));
            maestro.setCv(rs.getString("CV"));
            maestro.setGrado(rs.getString("GradoEstudios"));
            maestro.setPasswordTemp(rs.getString("PasswordTemporal"));
            maestro.setFoto(rs.getString("Foto"));
            maestro.setId(rs.getString("IdDocente"));
            maestro.setPassword(rs.getString("Password"));
            return maestro;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean updateDocente(Maestro docenteModelo) {
        String sqlDocente = "UPDATE Docentes SET CURP = ?, Nombre = ?, ApellidoPaterno = ?, ApellidoMaterno = ?, Genero = ?, "
                + "CorreoPersonal = ?, CorreoInstitucional = ?, Domicilio = ?, Celular = ?, Estado = ?, Municipio = ?, CV = ?, GradoEstudios = ?, Foto = ? "
                + " WHERE IdDocente = ?";
        return dataConsumer.executeUpdate(sqlDocente,pstmt-> {
            pstmt.setString(1, docenteModelo.getCurp());
            pstmt.setString(2, docenteModelo.getNombre());
            pstmt.setString(3, docenteModelo.getApPat());
            pstmt.setString(4, docenteModelo.getApMat());
            pstmt.setString(5, docenteModelo.getGenero());
            pstmt.setString(6, docenteModelo.getCorreoPer());
            pstmt.setString(7, docenteModelo.getCorreoIns());
            pstmt.setString(8, docenteModelo.getDomicilio());
            pstmt.setString(9, docenteModelo.getCelular());
            pstmt.setString(10, docenteModelo.getEstado());
            pstmt.setString(11, docenteModelo.getMunicipio());
            pstmt.setString(12, docenteModelo.getCv());
            pstmt.setString(13, docenteModelo.getGrado());
            pstmt.setString(14, docenteModelo.getFoto());
            pstmt.setString(15, docenteModelo.getId());
        });
    }

    public boolean deleteDocente(String id) {
        String sql = "DELETE FROM Docentes WHERE IdDocente = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, id);
        });
    }

    public Maestro obtenerDocente(String rfc) {
        String sql = "SELECT * FROM Docentes WHERE RFC = ?";

        return dataConsumer.getData(sql, pstmt ->{
            pstmt.setString(1, rfc);
        },this::mapResultSetToDocente);
    }

    public Maestro obtenerDocentePorId(String rfc) {
        String sql = "SELECT * FROM Docentes WHERE IdDocente = ?";
        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, rfc);
        },this::mapResultSetToDocente);
    }

    public boolean crearDocente(Maestro docenteModelo) {
        String sqlDocente = "INSERT INTO Docentes (RFC, CURP, Nombre, ApellidoPaterno, ApellidoMaterno, Genero, CorreoPersonal, "
                + "CorreoInstitucional, Domicilio, Celular, Estado, Municipio, CV, GradoEstudios, PasswordTemporal, Foto )"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        return dataConsumer.executeUpdate(sqlDocente, pstmtDocente->{
            pstmtDocente.setString(1, docenteModelo.getRfc());
            pstmtDocente.setString(2, docenteModelo.getCurp());
            pstmtDocente.setString(3, docenteModelo.getNombre());
            pstmtDocente.setString(4, docenteModelo.getApPat());
            pstmtDocente.setString(5, docenteModelo.getApMat());
            pstmtDocente.setString(6, docenteModelo.getGenero());
            pstmtDocente.setString(7, docenteModelo.getCorreoPer());
            pstmtDocente.setString(8, docenteModelo.getCorreoIns());
            pstmtDocente.setString(9, docenteModelo.getDomicilio());
            pstmtDocente.setString(10, docenteModelo.getCelular());
            pstmtDocente.setString(11, docenteModelo.getEstado());
            pstmtDocente.setString(12, docenteModelo.getMunicipio());
            pstmtDocente.setString(13, docenteModelo.getCv());
            pstmtDocente.setString(14, docenteModelo.getGrado());
            pstmtDocente.setString(15, docenteModelo.getRfc());
            pstmtDocente.setString(16, docenteModelo.getFoto());
        });
    }

    public Optional<Integer> crearDocenteId(Maestro docenteModelo) {
        String sqlDocente = "INSERT INTO Docentes (RFC, CURP, Nombre, ApellidoPaterno, ApellidoMaterno, Genero, CorreoPersonal, "
                + "CorreoInstitucional, Domicilio, Celular, Estado, Municipio, CV, GradoEstudios, PasswordTemporal, Foto )"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        return dataConsumer.executeUpdateWithGeneratedKeys(sqlDocente, pstmtDocente->{
            pstmtDocente.setString(1, docenteModelo.getRfc());
            pstmtDocente.setString(2, docenteModelo.getCurp());
            pstmtDocente.setString(3, docenteModelo.getNombre());
            pstmtDocente.setString(4, docenteModelo.getApPat());
            pstmtDocente.setString(5, docenteModelo.getApMat());
            pstmtDocente.setString(6, docenteModelo.getGenero());
            pstmtDocente.setString(7, docenteModelo.getCorreoPer());
            pstmtDocente.setString(8, docenteModelo.getCorreoIns());
            pstmtDocente.setString(9, docenteModelo.getDomicilio());
            pstmtDocente.setString(10, docenteModelo.getCelular());
            pstmtDocente.setString(11, docenteModelo.getEstado());
            pstmtDocente.setString(12, docenteModelo.getMunicipio());
            pstmtDocente.setString(13, docenteModelo.getCv());
            pstmtDocente.setString(14, docenteModelo.getGrado());
            pstmtDocente.setString(15, docenteModelo.getRfc());
            pstmtDocente.setString(16, docenteModelo.getFoto());
        });
    }

    public Maestro obtenerDocentePorGrupoYMateria(String idGrupo, String idMateria) {
        String sql = "SELECT d.* FROM Docentes d "
                + "JOIN GruposMaterias gm ON d.IdDocente = gm.IdDocente "
                + "WHERE gm.IdGrupo = ? AND gm.IdMateria = ?";
        Maestro docente = null;

        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, idGrupo);
            pstmt.setString(2, idMateria);
        },this::mapResultSetToDocente);
    }

    /*
    // Método para obtener grupos por RFC
    public List<GrupoModelo> obtenerGruposPorRFC(String rfc) {
        List<GrupoModelo> grupos = new List<>();
        String sql = "SELECT g.* "
                + "FROM Grupos g "
                + "JOIN DocentesGrupos dg ON g.IdGrupo = dg.IdGrupo "
                + "JOIN Docentes d ON dg.IdDocente = d.RFC "
                + "WHERE d.RFC = ?";

        try (Connection conn = new Conexion().connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rfc);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                
                    GrupoModelo grupo = seleccionarGrupo(rs.getInt("IdGrupo"));
                    grupos.add(grupo);
                  
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener grupos por RFC", e);
        }
        return grupos;
    }*/
}
