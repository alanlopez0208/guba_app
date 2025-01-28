package com.guba.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.guba.app.service.local.database.DataConsumer;
import com.guba.app.models.Carrera;
import com.guba.app.models.Materia;

public class DAOMaterias {

    final String SELECT_ALL =
            """
            SELECT
                    m.IdMateria AS MateriaId,
                    m.Nombre AS MateriaNombre,
                	m.HBCA AS MateriaHCBA,
                	m.HTI AS MateriaHTI,
                	m.Semestre AS MateriaSemestre,
                	m.Creditos AS MateriasCreditos,
                	m.Clave AS MateriaClave,
                	m.Modalidad AS MateriaModalidad,
                    c.IdCarrera AS CarreraId,
                	c.Clave AS CarreraClave,
                    c.Nombre AS CarreraNombre,
                	c.HBCA AS CarreraHCBA,
                	c.HTI AS CarreraHTI,
                	c.Creditos AS CarreraCreditos,
                	c.TotalHoras AS CarrerasTotalHoras,
                	c.Modalidad AS CarreraModalidad,
                	c.TotalAsignaturas AS CarreraTotalAsignaturas
                FROM Materias AS m
                INNER JOIN Carreras AS c ON m.IdCarrera = c.IdCarrera
            """;
    private DataConsumer<Materia> dataConsumer;
    public DAOMaterias(){
        dataConsumer = new DataConsumer<>();
    }


    public List<Materia> getMaterias() {
        return dataConsumer.getList(SELECT_ALL, this::mapResultSetToMateria);
    }

    public List<Materia> getMateriasByCarreraAndSemestre(String idCarrera, String semestre) {
        String sentencia = SELECT_ALL+" WHERE m.IdCarrera = ? AND m.Semestre = ?";
        return dataConsumer.getList(sentencia, t -> {
            t.setString(1, idCarrera);
            t.setString(2, semestre);
        },this::mapResultSetToMateria);
    }


    public boolean eliminarMateria(String id) {
        String sql = "DELETE FROM Materias WHERE IdMateria = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, id);
        });
    }

    public boolean insertMateria(Materia materia) {
        String sentencia = "INSERT INTO MATERIAS (Nombre, HBCA, HTI, Semestre, Creditos, IdCarrera, Clave, Modalidad ) VALUES (?,?,?,?,?,?,?,?)";
        return dataConsumer.executeUpdate(sentencia, pstmt->{
            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getHcba());
            pstmt.setString(3, materia.getHti());
            pstmt.setString(4, materia.getSemestre());
            pstmt.setString(5, materia.getCreditos());
            pstmt.setString(6, materia.getCarreraModelo().getIdCarrera());
            pstmt.setString(7, materia.getClave());
            pstmt.setString(8, materia.getModalidad());
        });
    }

    public boolean updateMateria(Materia materia) {
        String sentencia = "UPDATE Materias set Nombre = ?, HBCA = ?, HTI = ? , Semestre = ? , Creditos = ? , IdCarrera = ?, Clave = ? , Modalidad = ? WHERE IdMateria = ?";
        return dataConsumer.executeUpdate(sentencia, pstmt->{
            pstmt.setString(1, materia.getNombre());
            pstmt.setString(2, materia.getHcba());
            pstmt.setString(3, materia.getHti());
            pstmt.setString(4, materia.getSemestre());
            pstmt.setString(5, materia.getCreditos());
            pstmt.setString(6, materia.getCarreraModelo().getIdCarrera());
            pstmt.setString(7, materia.getClave());
            pstmt.setString(8, materia.getModalidad());
            pstmt.setString(9, materia.getIdMateria());
        });
    }

    public List<Materia> buscarMaterias(String where, String filtro) {
        ArrayList<Materia> resultados = new ArrayList<>();
        String sql = SELECT_ALL+"WHERE " + where + " LIKE ?";

        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, filtro + "%");
        },this::mapResultSetToMateria);
    }

    public Materia getMateria(String idMateria) {
        String sentencia = SELECT_ALL + " WHERE m.IdMateria = ?";
        return dataConsumer.getData(sentencia, pstmt->{
            pstmt.setString(1, idMateria);
        }, this::mapResultSetToMateria);
    }

    public List<Materia> getMateriasByCarreraSemestreSinElegir(String idCarrera, String semestre, String idGrupo) {
        ArrayList<Materia> materias = new ArrayList<>();
        String sentencia = SELECT_ALL+" WHERE m.IdCarrera = ? AND m.Semestre = ? AND m.IdMateria NOT IN (SELECT IdMateria FROM GruposMaterias WHERE IdGrupo = ?)";

        return dataConsumer.getList(sentencia, pstmt->{
            pstmt.setString(1, idCarrera);
            pstmt.setString(2, semestre);
            pstmt.setString(3, idGrupo);
        },this::mapResultSetToMateria);
    }


    public List<Materia> getMateriasByIdGrupo(String idGrupo) {
        ArrayList<Materia> materias = new ArrayList<>();
        String sentencia = SELECT_ALL + "JOIN GruposMaterias gm ON m.IdMateria = gm.IdMateria "
                + "WHERE gm.IdGrupo = ?";

        return dataConsumer.getList(sentencia, pstmt->{
            pstmt.setString(1, idGrupo);
        }, this::mapResultSetToMateria);
    }
    
    public boolean insertGrupoMateria(int idGrupo, int idMateria, int idDocente, int idPeriodo, int cursada) {
        String sentencia = "INSERT INTO GruposMaterias (IdGrupo, IdMateria, IdDocente, idPeriodo, Cursada) "
            + "VALUES (?, ?, ?, ?, ?)";
        return dataConsumer.executeUpdate(sentencia, pstmt->{
            pstmt.setInt(1, idGrupo);
            pstmt.setInt(2, idMateria);
            pstmt.setInt(3, idDocente);
            pstmt.setInt(4, idPeriodo);
            pstmt.setInt(5, cursada);
        });
    }

    public List<Materia> getMateriasByGrupo(String idGrupo){
      String sql = SELECT_ALL+" INNER JOIN GruposMaterias as gm ON gm.IdMateria = m.IdMateria WHERE gm.IdGrupo = ?";

      return dataConsumer.getList(sql, pstmt->{
          pstmt.setString(1, idGrupo);
      }, this::mapResultSetToMateria);
    };

    private Materia mapResultSetToMateria(ResultSet rs){
        try {
            Materia materia = new Materia();
            materia.setCarreraModelo(new Carrera());
            materia.setNombre(rs.getString("MateriaNombre"));
            materia.setIdMateria(rs.getString("MateriaId"));
            materia.setHcba(rs.getString("MateriaHCBA"));
            materia.setHti(rs.getString("MateriaHTI"));
            materia.setSemestre(rs.getString("MateriaSemestre"));
            materia.setCreditos(rs.getString("MateriasCreditos"));
            materia.setClave(rs.getString("MateriaClave"));
            materia.setModalidad(rs.getString("MateriaModalidad"));
            materia.getCarreraModelo().setIdCarrera(rs.getString("CarreraId"));
            materia.getCarreraModelo().setCreditos(rs.getString("CarreraCreditos"));
            materia.getCarreraModelo().setIdClave(rs.getString("CarreraClave"));
            materia.getCarreraModelo().setNombre(rs.getString("CarreraNombre"));
            materia.getCarreraModelo().setHbca(rs.getString("CarreraHCBA"));
            materia.getCarreraModelo().setHti(rs.getString("CarreraHTI"));
            materia.getCarreraModelo().setTotalHoras(rs.getString("CarrerasTotalHoras"));
            materia.getCarreraModelo().setModalidad(rs.getString("CarreraModalidad"));
            materia.getCarreraModelo().setTotalAsignaturas(rs.getString("CarreraTotalAsignaturas"));
            return materia;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
