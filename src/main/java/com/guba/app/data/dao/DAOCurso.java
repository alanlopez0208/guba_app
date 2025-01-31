package com.guba.app.data.dao;

import com.guba.app.domain.models.Curso;
import com.guba.app.domain.models.Participante;
import com.guba.app.data.local.database.DataConsumer;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class DAOCurso {
    private final DataConsumer<Curso> dataConsumer;

    public DAOCurso() {
        this.dataConsumer = new DataConsumer<>();
    }

    public Optional<Integer> insertarCurso(Curso curso) {
        String sqlCurso = "INSERT INTO Cursos (Nombre, Modalidad, FechaFin, FechaRealizacion, FechaIncio, Duracion) VALUES (?, ?, ?, ?, ?,?)";
        String sqlImpartidores = "INSERT INTO Asesores (IdCurso, Nombre, Puesto, Lugar) VALUES (?,?,?,?)";
        String sqlParticipantes = "INSERT INTO Participantes (Nombre, IdCurso) VALUES (?,?)";


        return dataConsumer.executeTransaction(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sqlCurso, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, curso.getNombre());
            pstmt.setString(2, curso.getModalidad());
            if (curso.getDateFin() != null){
                pstmt.setString(3, curso.dateToStringBD(curso.getDateFin()));
            }else{
                pstmt.setNull(3,Types.NULL);
            }
            pstmt.setString(4, curso.dateToStringBD(curso.getDateRealizacion()));
            pstmt.setString(5,curso.dateToStringBD(curso.getDateInicio()));
            pstmt.setInt(6,curso.getDuracionHoras());


            int isExceute = pstmt.executeUpdate();

            if (isExceute == 0){
                throw new SQLException("Error al Insertar el curso");
            }

            int key;
            try(ResultSet resultSet= pstmt.getGeneratedKeys()){
                if (resultSet.next()){
                    key = resultSet.getInt(1);
                }else{
                    throw new SQLException("Error al Insertar el Id del Curso");
                }
            }

            pstmt = connection.prepareStatement(sqlImpartidores);

            pstmt.setInt(1, key);
            pstmt.setString(2, curso.getAsesor().getNombre());
            pstmt.setString(3,curso.getAsesor().getPuesto());
            pstmt.setString(4,curso.getAsesor().getLugar());


            int rowAffectedImpartidor = pstmt.executeUpdate();

            if (rowAffectedImpartidor == 0 ){
                throw new SQLException("Error al Insertar el impartidor");
            }

            pstmt = connection.prepareStatement(sqlParticipantes);
            for (Participante participante : curso.getParticipantes()) {
                pstmt.setString(1, participante.getNombre());
                pstmt.setInt(2,key);
                pstmt.addBatch();
            }

            int[] rowAffected = pstmt.executeBatch();

            for (int i : rowAffected) {
                if (i <= 0){
                    throw new SQLException("Error al Insertar particpantes");
                }
            }

            return key;
        });
    }

    public List<Curso> obtenerTodosLosCursos() {
        String sql = "SELECT * FROM Cursos";
        List<Curso>  cursos = dataConsumer.getList(sql, this::mapConvertetResultSet);
        System.out.println("LA LISTA DE LOS CURSOS ES DE: " + cursos);
        return cursos;
    }

    public Optional<Curso> obtenerCursoPorId(int id) {
        String sql = "SELECT * FROM Cursos WHERE IdCurso = ?";
        Curso curso = dataConsumer.getData(sql, pstmt -> pstmt.setInt(1, id), this::mapConvertetResultSet);
        return Optional.ofNullable(curso);
    }

    public boolean actualizarCurso(Curso curso) {
        String sql = "UPDATE Cursos SET Nombre = ?, Modalidad = ?, FechaFin = ?, FechaRealizacion = ?, FechaIncio = ?, Duracion = ? WHERE IdCurso = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> {
            pstmt.setString(1, curso.getNombre());
            pstmt.setString(2, curso.getModalidad());
            pstmt.setString(3, curso.dateToStringBD(curso.getDateFin()));
            pstmt.setString(4, curso.dateToStringBD(curso.getDateRealizacion()));
            pstmt.setString(5,curso.dateToStringBD(curso.getDateInicio()));
            pstmt.setInt(6, curso.getDuracionHoras());
            pstmt.setInt(7, curso.getIdCurso());
        });
    }

    public boolean eliminarCurso(int id) {
        String sql = "DELETE FROM Cursos WHERE IdCurso = ?";
        return dataConsumer.executeUpdate(sql, pstmt -> pstmt.setInt(1, id));
    }

    private Curso mapConvertetResultSet(ResultSet resultSet) {
        try {
            Curso curso = new Curso();
            curso.setIdCurso(resultSet.getInt("IdCurso"));
            curso.setNombre(resultSet.getString("Nombre"));
            curso.setModalidad(resultSet.getString("Modalidad"));
            if (resultSet.getString("FechaFin") != null)
                curso.setDateFin(curso.stringToDateBd(resultSet.getString("FechaFin")));

            curso.setDateRealizacion(curso.stringToDateBd(resultSet.getString("FechaRealizacion")));
            curso.setDateInicio(curso.stringToDateBd(resultSet.getString("FechaIncio")));
            curso.setDuracionHoras(resultSet.getInt("Duracion"));
            return curso;
        } catch (SQLException e) {
            throw new RuntimeException("Error al mapear el ResultSet a Curso", e);
        }
    }
}
