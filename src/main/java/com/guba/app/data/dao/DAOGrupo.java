package com.guba.app.data.dao;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Carrera;
import com.guba.app.domain.models.Grupo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DAOGrupo {

    private DataConsumer<Grupo> dataConsumer;

    public DAOGrupo(){
        dataConsumer = new DataConsumer<>();
    }

    private String SELECT_ALL = """
            SELECT 
                gm.IdGrupo AS IDGrupo,
                gm.Nombre AS GrupoNombre,
                gm.Semestre AS GrupoSemestre,
                c.IdCarrera AS CarreraId,
                	c.Clave AS CarreraClave,
                    c.Nombre AS CarreraNombre,
                	c.HBCA AS CarreraHCBA,
                	c.HTI AS CarreraHTI,
                	c.Creditos AS CarreraCreditos,
                	c.TotalHoras AS CarrerasTotalHoras,
                	c.Modalidad AS CarreraModalidad,
                	c.TotalAsignaturas AS CarreraTotalAsignaturas
                FROM Grupos AS gm INNER JOIN Carreras AS c ON gm.IdCarrera = c.IdCarrera
            """;


    private Grupo mapResultSetToGrupo(ResultSet rs) {
        Grupo grupo = new Grupo();
        grupo.setCarrera(new Carrera());
        try {
            grupo.setId(rs.getString("IDGrupo"));
            grupo.setNombre(rs.getString("GrupoNombre"));
            grupo.setSemestre(rs.getString("GrupoSemestre"));
            grupo.getCarrera().setIdCarrera(rs.getString("CarreraId"));
            grupo.getCarrera().setCreditos(rs.getString("CarreraCreditos"));
            grupo.getCarrera().setIdClave(rs.getString("CarreraClave"));
            grupo.getCarrera().setNombre(rs.getString("CarreraNombre"));
            grupo.getCarrera().setHbca(rs.getString("CarreraHCBA"));
            grupo.getCarrera().setHti(rs.getString("CarreraHTI"));
            grupo.getCarrera().setTotalHoras(rs.getString("CarrerasTotalHoras"));
            grupo.getCarrera().setModalidad(rs.getString("CarreraModalidad"));
            grupo.getCarrera().setTotalAsignaturas(rs.getString("CarreraTotalAsignaturas"));
            return grupo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Grupo> getGrupos() {
        ArrayList<Grupo> listaGrupos = new ArrayList<>();
        return dataConsumer.getList(SELECT_ALL, this::mapResultSetToGrupo);
    }

    /*
    public ArrayList<MateriaModelo> getMateriasByGrupo(String idGrupo) {
        ArrayList<MateriaModelo> materias = new ArrayList<>();
        String sql = """
                SELECT m.IdMateria, m.IdCarrera, m.Nombre, m.HBCA, m.HTI, m.Semestre, m.Creditos, m.Clave
                FROM GruposMaterias as gm
                JOIN Materias as m ON m.IdMateria = gm.IdMateria
                JOIN Grupos as g ON g.IdGrupo = gm.IdGrupo
                WHERE g.IdGrupo = ?""";

        try (Connection conn = new Conexion().connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idGrupo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    MateriaModelo materia = new MateriaModelo();
                    materia.setNombre(rs.getString("Nombre"));
                    materia.setIdMateria(rs.getString("IdMateria"));
                    materia.setHcba(rs.getString("HBCA"));
                    materia.setHti(rs.getString("HTI"));
                    materia.setSemestre(rs.getString("Semestre"));
                    materia.setCreditos(rs.getString("Creditos"));
                    materia.setCarrera(rs.getString("IdCarrera"));
                    materia.setClave(rs.getString("Clave"));
                    materias.add(materia);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las materias del grupo: " + e.getMessage());
        }
        return materias;
    }*/

    public Optional<Integer> agregarGrupo(Grupo grupo) {
        String sql = "INSERT INTO Grupos (Nombre, Semestre, IdCarrera) VALUES (?, ?, ?)";

        return dataConsumer.executeUpdateWithGeneratedKeys(sql,pstm->{
            pstm.setString(1, grupo.getNombre());
            pstm.setString(2, grupo.getSemestre());
            pstm.setString(3, grupo.getCarrera().getIdCarrera());
        });
    }




    public boolean eliminarGrupo(String idGrupo) {
        String sql = "DELETE FROM Grupos WHERE IdGrupo = ?";
        return dataConsumer.executeUpdate(sql,psmt->{
            psmt.setString(1, idGrupo);
        });
    }

    public boolean actualizarGrupo(Grupo grupo) {
        String sql = "UPDATE Grupos SET Nombre = ?, Semestre = ?, IdCarrera = ? WHERE IdGrupo = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, grupo.getNombre());
            pstmt.setString(2, grupo.getSemestre());
            pstmt.setString(3, grupo.getCarrera().getIdCarrera());
            pstmt.setString(4, grupo.getId());
        });
    }

    public boolean eliminarMaterias(String idGrupo) {
        String sql = "DELETE FROM GruposMaterias WHERE IdGrupo = ?";
        return dataConsumer.executeUpdate(sql,pstmt->{
            pstmt.setInt(1, Integer.parseInt(idGrupo));
        });
    }


    public Grupo seleccionarGrupo(String idGrupo) {
        String sql = SELECT_ALL+" WHERE gm.IdGrupo = ?";
        Grupo grupoSeleccionado = null;

        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, idGrupo);
        }, this::mapResultSetToGrupo);
    }


    public List<Grupo> buscarGrupos(String where, String filtro) {
        ArrayList<Grupo> resultados = new ArrayList<>();
        String sql = "SELECT * FROM Grupos WHERE " + where + " LIKE ? ";

        return dataConsumer.getList(sql,pstmt->{
            pstmt.setString(1, filtro + "%");
        },this::mapResultSetToGrupo);
    }


    public Grupo getGrupoByCarreraAndSemestre(String idCarrera, String semestre) {
        String sql = SELECT_ALL+ " WHERE g.IdCarrera = ? AND g.Semestre = ?";

        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, idCarrera);
            pstmt.setString(2, semestre);
        },this::mapResultSetToGrupo);
    }
}
