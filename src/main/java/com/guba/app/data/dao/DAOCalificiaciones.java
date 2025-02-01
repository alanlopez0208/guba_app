package com.guba.app.data.dao;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import com.guba.app.domain.models.Materia;
import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Calificacion;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.Periodo;

public class DAOCalificiaciones {

    private DataConsumer<Calificacion> dataConsumer = new DataConsumer<>();

    final String SELECT_ALL = """
            SELECT
                Calificaciones.*,
                
                Alumnos.IdAlumno AS Alumno_Id,
                Alumnos.Matricula AS Alumno_Matricula,
                Alumnos.Nombre AS Alumno_Nombre,
                Alumnos.ApellidoPaterno AS Alumno_ApellidoPaterno,
                Alumnos.ApellidoMaterno AS Alumno_ApellidoMaterno,
                
                Docentes.IdDocente AS Docente_Id,
                Docentes.RFC AS Docente_RFC,
                Docentes.Nombre AS Docente_Nombre,
                Docentes.ApellidoPaterno AS Docente_ApellidoPaterno,
                Docentes.ApellidoMaterno AS Docente_ApellidoMaterno,
                
                Materias.IdMateria AS Materia_Id,
                Materias.Nombre AS Materia_Nombre,
                Materias.Creditos AS Materia_Creditos,
                Materias.Clave AS Materia_Clave,
                Materias.Semestre AS Materia_Semestre,
                 
                Periodo.IdPeriodo AS Periodo_Id,
                Periodo.Nombre AS Periodo_Nombre        
            FROM
                Calificaciones
            LEFT JOIN Alumnos ON Calificaciones.IdAlumno = Alumnos.IdAlumno
            LEFT JOIN Docentes ON Calificaciones.IdDocente = Docentes.IdDocente
            LEFT JOIN Materias ON Calificaciones.IdMateria = Materias.IdMateria
            LEFT JOIN Periodo ON Calificaciones.IdPeriodo = Periodo.IdPeriodo        
            """;


    private Calificacion mapResultSetToCalificacion(ResultSet rs)  {
        try {
            Calificacion calificacion = new Calificacion();
            Estudiante estudiante = new Estudiante();
            estudiante.setId(rs.getString("Alumno_Id"));
            estudiante.setMatricula(rs.getString("Alumno_Matricula"));
            estudiante.setNombre(rs.getString("Alumno_Nombre"));
            estudiante.setApMaterno(rs.getString("Alumno_ApellidoMaterno"));
            estudiante.setApPaterno(rs.getString("Alumno_ApellidoMaterno"));
            calificacion.setEstudiante(estudiante);
            calificacion.setIdCalificacion(rs.getInt("IdCalificacion"));
            calificacion.setIdAlumno(rs.getInt("IdAlumno"));
            calificacion.setIdDocente(rs.getInt("IdDocente"));
            calificacion.setMateria(new Materia());
            calificacion.getMateria().setId(rs.getString("Materia_Id"));
            calificacion.getMateria().setNombre(rs.getString("Materia_Nombre"));
            calificacion.getMateria().setCreditos(rs.getString("Materia_Creditos"));
            calificacion.getMateria().setClave(rs.getString("Materia_Clave"));
            calificacion.getMateria().setSemestre(rs.getString("Materia_Semestre"));
            Periodo periodo = new Periodo();
            periodo.setId(rs.getString("Periodo_Id"));
            periodo.setNombre(rs.getString("Periodo_Nombre"));
            calificacion.setPerido(periodo);


            Float p1u1 = rs.getFloat("P1U1");
            calificacion.setP1U1(rs.wasNull() ? null : p1u1);

            Float p2u1 = rs.getFloat("P2U1");
            calificacion.setP2U1(rs.wasNull() ? null : p2u1);

            Float p3u1 = rs.getFloat("P3U1");
            calificacion.setP3U1(rs.wasNull() ? null : p3u1);

            Float p4u1 = rs.getFloat("P4U1");
            calificacion.setP4U1(rs.wasNull() ? null : p4u1);

            Float p1u2 = rs.getFloat("P1U2");
            calificacion.setP1U2(rs.wasNull() ? null : p1u2);

            Float p2u2 = rs.getFloat("P2U2");
            calificacion.setP2U2(rs.wasNull() ? null : p2u2);

            Float p3u2 = rs.getFloat("P3U2");
            calificacion.setP3U2(rs.wasNull() ? null : p3u2);

            Float p4u2 = rs.getFloat("P4U2");
            calificacion.setP4U2(rs.wasNull() ? null : p4u2);

            Float p1u3 = rs.getFloat("P1U3");
            calificacion.setP1U3(rs.wasNull() ? null : p1u3);

            Float p2u3 = rs.getFloat("P2U3");
            calificacion.setP2U3(rs.wasNull() ? null : p2u3);

            Float p3u3 = rs.getFloat("P3U3");
            calificacion.setP3U3(rs.wasNull() ? null : p3u3);

            Float p4u3 = rs.getFloat("P4U3");
            calificacion.setP4U3(rs.wasNull() ? null : p4u3);

            Float p1u4 = rs.getFloat("P1U4");
            calificacion.setP1U4(rs.wasNull() ? null : p1u4);

            Float p2u4 = rs.getFloat("P2U4");
            calificacion.setP2U4(rs.wasNull() ? null : p2u4);

            Float p3u4 = rs.getFloat("P3U4");
            calificacion.setP3U4(rs.wasNull() ? null : p3u4);

            Float p4u4 = rs.getFloat("P4U4");
            calificacion.setP4U4(rs.wasNull() ? null : p4u4);

            Float tb = rs.getFloat("TB");
            calificacion.setTrabjoFinal(rs.wasNull() ? null : tb);

            String tipo = rs.getString("Tipo");
            calificacion.setTipo(tipo);

            String fecha = rs.getString("Fecha");
            calificacion.setFecha(fecha);

            String folio = rs.getString("Folio");
            calificacion.setModulo(folio);

            calificacion.establecerPromedioFinal();
            return calificacion;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Calificacion> obtenerCalificaciones(String idAlumno) {
        String sql = SELECT_ALL+ " WHERE Calificaciones.IdAlumno = ? ";


        return dataConsumer.getList(sql,pstmt->{
            pstmt.setString(1, idAlumno);
        },this::mapResultSetToCalificacion);
    }

    public List<Calificacion> obtenerByMateriaAndGrupo(String idMateria, String idGrupo) {
        String sql = SELECT_ALL + " WHERE Calificaciones.IdMateria = ? AND Alumnos.IdGrupo = ?";

        return dataConsumer.getList(sql, pstmt -> {
            pstmt.setString(1, idMateria);
            pstmt.setString(2, idGrupo);
        }, this::mapResultSetToCalificacion);
    }
    public Optional<Boolean> insertarCalificaciones(List<Estudiante> estudiantes, String idDocente, String idMateria, String IdGrupo) {
        String sqlActualizarGrupo = "UPDATE Alumnos SET IdGrupo = ? WHERE IdAlumno = ?";
        String sqlVerificacion = "SELECT COUNT(*) FROM Calificaciones WHERE IdAlumno = ? AND IdMateria = ?";
        String sqlInsercion = "INSERT INTO Calificaciones (IdAlumno,  IdDocente, IdMateria, IdPeriodo) VALUES (?, ?, ?, (SELECT IdPeriodo FROM Periodo ORDER BY IdPeriodo DESC LIMIT 1))";

        return dataConsumer.executeTransaction(connection -> {
            try (PreparedStatement pstmtVerificacion = connection.prepareStatement(sqlVerificacion);
                 PreparedStatement pstmtInsercion = connection.prepareStatement(sqlInsercion, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement pstmtActualizarGrupo = connection.prepareStatement(sqlActualizarGrupo)) {

                for (Estudiante estudiante : estudiantes) {

                    pstmtVerificacion.setString(1, estudiante.getId());
                    pstmtVerificacion.setString(2, idMateria);

                    try (ResultSet rsVerificacion = pstmtVerificacion.executeQuery()) {
                        if (rsVerificacion.next() && rsVerificacion.getInt(1) == 0) {

                            pstmtInsercion.setString(1, estudiante.getId());
                            pstmtInsercion.setString(2, idDocente);
                            pstmtInsercion.setString(3, idMateria);
                            pstmtInsercion.addBatch();


                            pstmtActualizarGrupo.setString(1, IdGrupo);
                            pstmtActualizarGrupo.setString(2, estudiante.getId());
                            pstmtActualizarGrupo.addBatch();
                        } else {
                            System.out.println("El registro ya existe para IdAlumno: " + estudiante.getId() + " de IdMateria: " + idMateria);
                            throw new SQLException("El registro ya existe");
                        }
                    }
                }


                int[] rowsAffectedInsercion = pstmtInsercion.executeBatch();
                for (int rows : rowsAffectedInsercion) {
                    if (rows == PreparedStatement.EXECUTE_FAILED) {
                        throw new SQLException("Error al insertar las calificaciones");
                    }
                }


                int[] rowsAffectedGrupo = pstmtActualizarGrupo.executeBatch();
                for (int rows : rowsAffectedGrupo) {
                    if (rows == PreparedStatement.EXECUTE_FAILED) {
                        throw new SQLException("Error al actualizar el grupo de los alumnos");
                    }
                }

                return true;
            }
        });
    }


    public boolean updateCalificacion(Calificacion calificacion) {
        String sql = "UPDATE Calificaciones SET IdAlumno = ?, IdMateria = ?,"
                + "P1U1 = ?, P2U1 = ?, P3U1 = ?, P4U1 = ?, "
                + "P1U2 = ?, P2U2 = ?, P3U2 = ?, P4U2 = ?, "
                + "P1U3 = ?, P2U3 = ?, P3U3 = ?, P4U3 = ?, "
                + "P1U4 = ?, P2U4 = ?, P3U4 = ?, P4U4 = ?, "
                + "TB = ?"
                + "WHERE IdCalificacion = ?";


        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setInt(1, calificacion.getIdAlumno());
            //   pstmt.setInt(2, calificacion.getIdDocente());
            pstmt.setInt(2, Integer.parseInt(calificacion.getMateria().getId()));
            // pstmt.setString(3, calificacion.getGrupo().getId());

            if (calificacion.getP1U1() != null) {

                pstmt.setFloat(3, calificacion.getP1U1());
            } else {
                pstmt.setNull(3, java.sql.Types.FLOAT);
            }
            if (calificacion.getP2U1() != null) {
                pstmt.setFloat(4, calificacion.getP2U1());
            } else {
                pstmt.setNull(4, java.sql.Types.FLOAT);
            }
            if (calificacion.getP3U1() != null) {
                pstmt.setFloat(5, calificacion.getP3U1());
            } else {
                pstmt.setNull(5, java.sql.Types.FLOAT);
            }
            if (calificacion.getP4U1() != null) {
                pstmt.setFloat(6, calificacion.getP4U1());
            } else {
                pstmt.setNull(6, java.sql.Types.FLOAT);
            }
            if (calificacion.getP1U2() != null) {
                pstmt.setFloat(7, calificacion.getP1U2());
            } else {
                pstmt.setNull(7, java.sql.Types.FLOAT);
            }
            if (calificacion.getP2U2() != null) {
                pstmt.setFloat(8, calificacion.getP2U2());
            } else {
                pstmt.setNull(8, java.sql.Types.FLOAT);
            }
            if (calificacion.getP3U2() != null) {
                pstmt.setFloat(9, calificacion.getP3U2());
            } else {
                pstmt.setNull(9, java.sql.Types.FLOAT);
            }
            if (calificacion.getP4U2() != null) {
                pstmt.setFloat(10, calificacion.getP4U2());
            } else {
                pstmt.setNull(10, java.sql.Types.FLOAT);
            }

            if (calificacion.getP1U3() != null) {
                pstmt.setFloat(11, calificacion.getP1U3());
            } else {
                pstmt.setNull(11, java.sql.Types.FLOAT);
            }

            if (calificacion.getP2U3() != null) {
                pstmt.setFloat(12, calificacion.getP2U3());
            } else {
                pstmt.setNull(12, java.sql.Types.FLOAT);
            }

            if (calificacion.getP3U3() != null) {
                pstmt.setFloat(13, calificacion.getP3U3());
            } else {
                pstmt.setNull(13, java.sql.Types.FLOAT);
            }

            if (calificacion.getP4U3() != null) {
                pstmt.setFloat(14, calificacion.getP4U3());
            } else {
                pstmt.setNull(14, java.sql.Types.FLOAT);
            }

            if (calificacion.getP1U4() != null) {
                pstmt.setFloat(15, calificacion.getP1U4());
            } else {
                pstmt.setNull(15, java.sql.Types.FLOAT);
            }

            if (calificacion.getP2U4() != null) {
                pstmt.setFloat(16, calificacion.getP2U4());
            } else {
                pstmt.setNull(16, java.sql.Types.FLOAT);
            }

            if (calificacion.getP3U4() != null) {
                pstmt.setFloat(17, calificacion.getP3U4());
            } else {
                pstmt.setNull(17, java.sql.Types.FLOAT);
            }

            if (calificacion.getP4U4() != null) {
                pstmt.setFloat(18, calificacion.getP4U4());
            } else {
                pstmt.setNull(18, java.sql.Types.FLOAT);
            }

            if (calificacion.getTrabjoFinal() != null) {
                pstmt.setFloat(19, calificacion.getTrabjoFinal());
            } else {
                pstmt.setNull(19, java.sql.Types.FLOAT);
            }
            pstmt.setInt(20, calificacion.getIdCalificacion());
        });
    }


    public boolean deleteCalificacion(int idCalificacion) {
        String sql = "DELETE FROM Calificaciones WHERE IdCalificacion = ?";
        return dataConsumer.executeUpdate(sql,pstmt->{
            pstmt.setInt(1,idCalificacion);
        });
    }


    public Optional<Boolean> insertOrReplace(List<Calificacion> calificacions){
        String sql = "INSERT OR REPLACE INTO Calificaciones "
                + "( IdAlumno, IdMateria,"
                + "P1U1, P2U1, P3U1, P4U1, "
                + "P1U2, P2U2, P3U2, P4U2, "
                + "P1U3, P2U3, P3U3, P4U3, "
                + "P1U4, P2U4, P3U4, P4U4, "
                + "TB, idPeriodo, IdCalificacion)"
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return dataConsumer.executeTransaction(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for (Calificacion calificacion : calificacions) {
                pstmt.setInt(1, calificacion.getIdAlumno());
                //   pstmt.setInt(2, calificacion.getIdDocente());
                pstmt.setInt(2, calificacion.getIdMateria());
                // pstmt.setString(3, calificacion.getGrupo().getId());

                if (calificacion.getP1U1() != null) {
                    pstmt.setFloat(3, calificacion.getP1U1());
                } else {
                    pstmt.setNull(3, java.sql.Types.FLOAT);
                }
                if (calificacion.getP2U1() != null) {
                    pstmt.setFloat(4, calificacion.getP2U1());
                } else {
                    pstmt.setNull(4, java.sql.Types.FLOAT);
                }
                if (calificacion.getP3U1() != null) {
                    pstmt.setFloat(5, calificacion.getP3U1());
                } else {
                    pstmt.setNull(5, java.sql.Types.FLOAT);
                }
                if (calificacion.getP4U1() != null) {
                    pstmt.setFloat(6, calificacion.getP4U1());
                } else {
                    pstmt.setNull(6, java.sql.Types.FLOAT);
                }
                if (calificacion.getP1U2() != null) {
                    pstmt.setFloat(7, calificacion.getP1U2());
                } else {
                    pstmt.setNull(7, java.sql.Types.FLOAT);
                }
                if (calificacion.getP2U2() != null) {
                    pstmt.setFloat(8, calificacion.getP2U2());
                } else {
                    pstmt.setNull(8, java.sql.Types.FLOAT);
                }
                if (calificacion.getP3U2() != null) {
                    pstmt.setFloat(9, calificacion.getP3U2());
                } else {
                    pstmt.setNull(9, java.sql.Types.FLOAT);
                }
                if (calificacion.getP4U2() != null) {
                    pstmt.setFloat(10, calificacion.getP4U2());
                } else {
                    pstmt.setNull(10, java.sql.Types.FLOAT);
                }

                if (calificacion.getP1U3() != null) {
                    pstmt.setFloat(11, calificacion.getP1U3());
                } else {
                    pstmt.setNull(11, java.sql.Types.FLOAT);
                }

                if (calificacion.getP2U3() != null) {
                    pstmt.setFloat(12, calificacion.getP2U3());
                } else {
                    pstmt.setNull(12, java.sql.Types.FLOAT);
                }

                if (calificacion.getP3U3() != null) {
                    pstmt.setFloat(13, calificacion.getP3U3());
                } else {
                    pstmt.setNull(13, java.sql.Types.FLOAT);
                }

                if (calificacion.getP4U3() != null) {
                    pstmt.setFloat(14, calificacion.getP4U3());
                } else {
                    pstmt.setNull(14, java.sql.Types.FLOAT);
                }

                if (calificacion.getP1U4() != null) {
                    pstmt.setFloat(15, calificacion.getP1U4());
                } else {
                    pstmt.setNull(15, java.sql.Types.FLOAT);
                }

                if (calificacion.getP2U4() != null) {
                    pstmt.setFloat(16, calificacion.getP2U4());
                } else {
                    pstmt.setNull(16, java.sql.Types.FLOAT);
                }

                if (calificacion.getP3U4() != null) {
                    pstmt.setFloat(17, calificacion.getP3U4());
                } else {
                    pstmt.setNull(17, java.sql.Types.FLOAT);
                }

                if (calificacion.getP4U4() != null) {
                    pstmt.setFloat(18, calificacion.getP4U4());
                } else {
                    pstmt.setNull(18, java.sql.Types.FLOAT);
                }

                if (calificacion.getTrabjoFinal() != null) {
                    pstmt.setFloat(19, calificacion.getTrabjoFinal());
                } else {
                    pstmt.setNull(19, java.sql.Types.FLOAT);
                }
                pstmt.setInt(20, calificacion.getIdPerido());
                pstmt.setInt(21, calificacion.getIdCalificacion());
                pstmt.addBatch();
            }
            for (int rows : pstmt.executeBatch()) {
                if (rows == PreparedStatement.EXECUTE_FAILED) {
                    throw new SQLException("Error al Querer insertar las calificaciones");
                }
            }
            return true;
        });
    }

}
