package com.guba.app.data.dao;


import com.guba.app.data.local.database.conexion.Conexion;
import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Estudiante;
import javafx.scene.image.Image;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOAlumno {
    private DataConsumer<Estudiante> dataConsumer;

    public DAOAlumno(){
        dataConsumer = new DataConsumer<>();
    }

    public List<Estudiante> getEstudiantes() {
        String sql = "SELECT * FROM Alumnos ASC";
        return dataConsumer.getList(sql, this::mapResultSetToEstudiante);
    }

    public List<Estudiante> buscarEstudiantes(String where, String filtro) {
        ArrayList<Estudiante> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Alumnos WHERE " + where + " LIKE ?";

        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, filtro + "%");
        }, this::mapResultSetToEstudiante);
    }

    private Estudiante mapResultSetToEstudiante(ResultSet rs){
        Estudiante estudiante = new Estudiante();
        try {
            estudiante.setId(rs.getString("IdAlumno"));
            estudiante.setMatricula(rs.getString("Matricula"));
            estudiante.setNombre(rs.getString("Nombre"));
            estudiante.setApPaterno(rs.getString("ApellidoPaterno"));
            estudiante.setApMaterno(rs.getString("ApellidoMaterno"));
            estudiante.setEmailPersonal(rs.getString("CorreoPersonal"));
            estudiante.setEmailInstitucional(rs.getString("CorreoInstitucional"));
            estudiante.setGeneracion(rs.getString("Generacion"));
            estudiante.setNumCelular(rs.getString("Celular"));
            estudiante.setEstado(rs.getString("Estado"));
            estudiante.setMunicipio(rs.getString("Municipio"));
            estudiante.setEscProcedencia(rs.getString("EscuelaProcedencia"));
            estudiante.setGrado(rs.getString("GradoEstudios"));
            estudiante.setGrupo(rs.getString("IdGrupo"));
            estudiante.setStatus(rs.getString("Status"));
            estudiante.setPassword(rs.getString("Password"));
            estudiante.setPasswordTemporal(rs.getString("PasswordTemporal"));
            estudiante.setSexo(rs.getString("Genero"));
            estudiante.setFoto(rs.getString("Foto"));
            DAOCarreras opCarrera = new DAOCarreras();
            estudiante.setCarrera(opCarrera.getCarreraByd(rs.getString("IdCarrera")));
            estudiante.setSemestre(rs.getString("Semestre"));
            estudiante.setDireccion(rs.getString("Direccion"));
            estudiante.setNacimiento(rs.getString("Nacimiento"));
            if (estudiante.getFoto() != null){
                try{
                    estudiante.setFotoPerfil(new Image(new File(estudiante.getFoto()).toURI().toString()));
                }catch (Exception e){
                    estudiante.setFotoPerfil(null);
                }
            }
            return estudiante;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAlumno(Estudiante estudiante) {
        String sql = "UPDATE Alumnos SET Nombre = ?, IdCarrera = ?, Semestre = ?, ApellidoPaterno = ?, ApellidoMaterno = ?, CorreoPersonal = ?, "
                + "CorreoInstitucional = ?, Generacion = ?, Celular = ?, Estado = ?, Municipio = ?, "
                + "EscuelaProcedencia = ?, GradoEstudios = ?, IdGrupo = ?, Status = ?, Genero = ?, "
                + "Password = ?, Direccion = ?, Nacimiento = ?,"
                + "Foto = ?, PasswordTemporal = ?"
                + " WHERE IdAlumno = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, estudiante.getNombre());
            pstmt.setString(2, estudiante.getCarrera().getIdCarrera());
            pstmt.setString(3, estudiante.getSemestre());
            pstmt.setString(4, estudiante.getApPaterno());
            pstmt.setString(5, estudiante.getApMaterno());
            pstmt.setString(6, estudiante.getEmailPersonal());
            pstmt.setString(7, estudiante.getEmailInstitucional());
            pstmt.setString(8, estudiante.getGeneracion());
            pstmt.setString(9, estudiante.getNumCelular());
            pstmt.setString(10, estudiante.getEstado());
            pstmt.setString(11, estudiante.getMunicipio());
            pstmt.setString(12, estudiante.getEscProcedencia());
            pstmt.setString(13, estudiante.getGrado());
            pstmt.setString(14, estudiante.getGrupo());
            pstmt.setString(15, estudiante.getStatus());
            pstmt.setString(16, estudiante.getSexo());
            pstmt.setString(17, estudiante.getPassword());
            pstmt.setString(18, estudiante.getDireccion());
            pstmt.setString(19, estudiante.getNacimiento());
            pstmt.setString(20,estudiante.getFoto());
            pstmt.setString(21, estudiante.getMatricula());
            pstmt.setString(22, estudiante.getId());
        });
    }

    public boolean deleteAlumno(String matricula) {
        String sql = "DELETE FROM Alumnos WHERE IdAlumno = ?";
        return  dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, matricula);
        });
    }

    public Estudiante getEstudiante(String matricula) {
        String sql = "SELECT * FROM Alumnos WHERE Matricula = ?";
        Estudiante estudiante = null;
        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, matricula);
        }, this::mapResultSetToEstudiante);
    }

    public List<Estudiante> getEstudiantesBYGrupo(String idGrupo) {
        String sql = "SELECT * FROM Alumnos WHERE IdGrupo = ?";
        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, idGrupo);
        }, this::mapResultSetToEstudiante);
    }

    public Optional<Integer> crearAlumno(Estudiante Estudiante) {
        String sqlAlumno = "INSERT INTO Alumnos (Matricula, IdCarrera, Semestre, Nombre, ApellidoPaterno, ApellidoMaterno, Genero, "
                + "CorreoPersonal, CorreoInstitucional, Generacion, Celular, Estado, Municipio, EscuelaProcedencia, GradoEstudios, "
                + "PasswordTemporal, IdGrupo, Password, Status, Direccion, Nacimiento"
                + (Estudiante.getFoto() != null ? ", Foto" : "") + ") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
                + (Estudiante.getFoto() != null ? ", ?" : "") + ")";

        return dataConsumer.executeUpdateWithGeneratedKeys(sqlAlumno, pstmt->{
            pstmt.setString(1, Estudiante.getMatricula());
            pstmt.setString(2, Estudiante.getCarrera().getIdCarrera());
            pstmt.setString(3, Estudiante.getSemestre());
            pstmt.setString(4, Estudiante.getNombre());
            pstmt.setString(5, Estudiante.getApPaterno());
            pstmt.setString(6, Estudiante.getApMaterno());
            pstmt.setString(7, Estudiante.getSexo());
            pstmt.setString(8, Estudiante.getEmailPersonal());
            pstmt.setString(9, Estudiante.getEmailInstitucional());
            pstmt.setString(10, Estudiante.getGeneracion());
            pstmt.setString(11, Estudiante.getNumCelular());
            pstmt.setString(12, Estudiante.getEstado());
            pstmt.setString(13, Estudiante.getMunicipio());
            pstmt.setString(14, Estudiante.getEscProcedencia());
            pstmt.setString(15, Estudiante.getGrado());
            pstmt.setString(16, Estudiante.getPasswordTemporal());
            pstmt.setString(17, Estudiante.getGrupo());
            pstmt.setString(18, Estudiante.getPassword());
            pstmt.setString(19, Estudiante.getStatus());
            pstmt.setString(20, Estudiante.getDireccion());
            pstmt.setString(21, Estudiante.getNacimiento());
        });
    }

    public List<Estudiante> getEstudiantesByCarreraAndSemestre(String idCarrera, String semestre) {
        String sql = "SELECT * FROM Alumnos WHERE IdCarrera = ? AND Semestre = ?";
       return dataConsumer.getList(sql, pstmt->{
           pstmt.setString(1, idCarrera);
           pstmt.setString(2, semestre);
       },this::mapResultSetToEstudiante);
    }

    public boolean incrementarSemestre() {
        String sql = "UPDATE Alumnos "
                + "SET Semestre = CASE "
                + "    WHEN Semestre < 8 THEN Semestre + 1 "
                + "    WHEN Semestre = 8 THEN 9 "
                + "    ELSE Semestre "
                + "END, IdGrupo = SELECT idGrupo FROM Grupos WHERE Semestre = semestre";

        return dataConsumer.executeUpdate(sql);
    }

    public List<Estudiante> getEstudiantesByMateria(String idMateria, String idGrupo) {
        String sqlCalificaciones = "SELECT * From Alumnos as a INNER JOIN Calificaciones as c ON a.IdAlumno = c.IdAlumno WHERE a.IdGrupo = ? AND c.IdMateria = ?";


        return dataConsumer.getList(sqlCalificaciones, pstmt->{
            pstmt.setString(1, idGrupo);
            pstmt.setString(2, idMateria);

        },this::mapResultSetToEstudiante);
    }

    public List<Estudiante> getEstudiantesByCarreraAndMateriaSinCalificaciones(String idCarrera, String idMateria) {
        String sql = """
                SELECT A.*
                FROM Alumnos A
                LEFT JOIN Calificaciones C ON A.IdAlumno = C.IdAlumno AND C.IdMateria = ?
                WHERE A.IdCarrera = ? AND C.IdAlumno IS NULL;
        """;

        System.out.println("Materia: " + idMateria +" Carrera : " + idCarrera);
        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, idMateria);
            pstmt.setString(2, idCarrera);
        },this::mapResultSetToEstudiante);
    }

    public boolean insertarCalificacion(Estudiante alumno, String idDocente, String idMateria, String idPeriodo) {

        String sql = "INSERT INTO Calificaciones (IdAlumno, IdDocente, IdMateria, IdPeriodo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, alumno.getId()); // IdAlumno
            pstmt.setString(2, idDocente); // IdDocente
            pstmt.setString(3, idMateria); // IdMateria
            pstmt.setString(4, idPeriodo); // IdPeriodo

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar calificación: " + e.getMessage());
            return false;
        }
    }

    public boolean insertarCalificaciones(ArrayList<Estudiante> estudiantes, String idDocente, String idMateria, String idPeriodo) {
        String sqlVerificacion = "SELECT COUNT(*) FROM Calificaciones WHERE IdAlumno = ? AND IdMateria = ?";
        String sqlInsercion = "INSERT INTO Calificaciones (IdAlumno, IdDocente, IdMateria, IdPeriodo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmtVerificacion = conn.prepareStatement(sqlVerificacion); PreparedStatement pstmtInsercion = conn.prepareStatement(sqlInsercion)) {

            conn.setAutoCommit(false);

            boolean exito = true;

            for (Estudiante estudiante : estudiantes) {
                pstmtVerificacion.setString(1, estudiante.getId());
                pstmtVerificacion.setString(2, idMateria);

                try (ResultSet rsVerificacion = pstmtVerificacion.executeQuery()) {
                    if (rsVerificacion.next() && rsVerificacion.getInt(1) == 0) {
                        pstmtInsercion.setString(1, estudiante.getId());
                        pstmtInsercion.setString(2, idDocente);
                        pstmtInsercion.setString(3, idMateria);
                        pstmtInsercion.setString(4, idPeriodo);

                        pstmtInsercion.addBatch();
                    } else {
                        System.out.println("El registro ya existe para IdAlumno: " + estudiante.getId() + "de IdMateria: " + idMateria);
                        exito = false;
                    }
                }
            }

            int[] rowsAffected = pstmtInsercion.executeBatch();
            conn.commit();

            for (int rows : rowsAffected) {
                if (rows == PreparedStatement.EXECUTE_FAILED) {
                    conn.rollback();
                    return false;
                }
            }
            return exito;
        } catch (SQLException e) {
            System.out.println("Error al insertar calificaciones: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCalificaciones(String idAlumno, String idMateria) {
        String sql = "DELETE FROM Calificaciones WHERE IdAlumno = ? AND IdMateria = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAlumno);
            pstmt.setString(2, idMateria);

            int rowsAffected = pstmt.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar calificaciones: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarGrupoEstudiantes(String idGrupo, List<Estudiante> estudiantes) {
        String sql = "UPDATE Alumnos SET IdGrupo = ? WHERE Matricula = ?";
        return dataConsumer.excuteBatch(sql, pstmt->{
            for (Estudiante estudiante : estudiantes) {
                pstmt.setString(1, idGrupo);
                pstmt.setString(2, estudiante.getId());
            }
        });
    }

    public boolean actualizarIdGrupoEstudiante(String idGrupo, Estudiante estudiante) {
        String sql = "UPDATE Alumnos SET IdGrupo = ? WHERE Matricula = ?";

        try (Connection conn = Conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idGrupo);
            pstmt.setString(2, estudiante.getMatricula());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar IdGrupo del estudiante", e);
        }
    }


    public boolean updatePassword(Estudiante estudiante){
        String sql = "INSERT OR REPLACE Alumnos SET Password = ? WHERE IdAlumno = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
           pstmt.setString(1,estudiante.getPassword());
           pstmt.setString(2,estudiante.getPassword());
        });
    }

    public boolean changeDegree(String matricula, String idCarrera, String idAlumno){
        String sql = "UPDATE Alumnos SET Matricula = ?, IdCarrera = ? WHERE IdAlumno = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1,matricula);
            pstmt.setString(2, idCarrera);
            pstmt.setString(3,idAlumno);
        });
    }
}
