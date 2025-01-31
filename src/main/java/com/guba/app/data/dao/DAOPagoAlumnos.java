package com.guba.app.data.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.guba.app.data.local.database.DataConsumer;
import com.guba.app.domain.models.Estudiante;
import com.guba.app.domain.models.PagoAlumno;


public class DAOPagoAlumnos {
    private DataConsumer<PagoAlumno> dataConsumer = new DataConsumer<>();

    final String SELECT_ALL = """
                    SELECT 
                    p.IdPago AS PagoIdPago,
                    p.Fecha AS PagoFecha,
                	p.Cantidad AS PagoCantidad,
                	p.Concepto AS PagoConcepto,
                	p.Factura AS PagoFactura,
                	a.IdAlumno AS AlumnoIdAlumno,
                	a.Matricula AS AlumnoMatricula,
                	a.Nombre AS AlumnoNombre,
                    a.ApellidoPaterno AS AlumnoApellidoPaterno,
                	a.ApellidoMaterno AS AlumnoApellidoMaterno
                FROM PagoAlumnos AS p
                INNER JOIN Alumnos AS a ON p.IdAlumno = a.IdAlumno
            """;


    public List<PagoAlumno> getPagos() {
        return dataConsumer.getList(SELECT_ALL, this::mapResultSetToPago);
    }


    public List<PagoAlumno> buscarPagos(String where, String filtro) {

        String sql = SELECT_ALL+" WHERE " + where + " LIKE ?";
       return dataConsumer.getList(sql, pstmt->{
           pstmt.setString(1,filtro+"%");
       },this::mapResultSetToPago);
    }


    public boolean updatePago(PagoAlumno pagoModelo) {
        String sql = "UPDATE PagoAlumnos SET IdAlumno = ?, Fecha = ?, Cantidad = ?, Concepto = ?, Factura = ? WHERE IdPago = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, pagoModelo.getAlumno().getId());
            pstmt.setString(2, pagoModelo.toStringDate());
            pstmt.setString(3, pagoModelo.getCantidad());
            pstmt.setString(4, pagoModelo.getConcepto());
            pstmt.setString(5, pagoModelo.getFactura());
            pstmt.setString(6, pagoModelo.getIdPago());
        });
    }


    public boolean deletePago(String idPago) {
        String sql = "DELETE FROM PagoAlumnos WHERE IdPago = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, idPago);
        });
    }


    public PagoAlumno retornarPago(String idPago) {
        String sql = SELECT_ALL+ " WHERE IdPago = ?";

        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, idPago);
        },this::mapResultSetToPago);
    }

    public Optional<Integer> crearPago(PagoAlumno pagoModelo) {
        String sql = "INSERT INTO PagoAlumnos (IdPago, IdAlumno,Fecha, Cantidad, Concepto, Factura ) VALUES (?, ?, ?, ?, ?, ? )";
        return dataConsumer.executeUpdateWithGeneratedKeys(sql,pstmt->{
            pstmt.setString(1, pagoModelo.getIdPago());
            pstmt.setString(2, pagoModelo.getAlumno().getId());
            pstmt.setString(3, pagoModelo.toStringDate());
            pstmt.setString(4, pagoModelo.getCantidad());
            pstmt.setString(5, pagoModelo.getConcepto());
            pstmt.setString(6, pagoModelo.getFactura());
        });
    }

    private PagoAlumno mapResultSetToPago(ResultSet rs){
        try {
            PagoAlumno pago = new PagoAlumno();
            pago.setAlumno(new Estudiante());
            pago.getAlumno().setId(rs.getString("AlumnoIdAlumno"));
            pago.getAlumno().setMatricula(rs.getString("AlumnoMatricula"));
            pago.getAlumno().setNombre(rs.getString("AlumnoNombre"));
            pago.getAlumno().setApPaterno(rs.getString("AlumnoApellidoPaterno"));
            pago.getAlumno().setApMaterno(rs.getString("AlumnoApellidoMaterno"));
            pago.setIdPago(rs.getString("PagoIdPago"));
            pago.setFecha(rs.getString("PagoFecha"));
            pago.setCantidad(rs.getString("PagoCantidad"));
            pago.setConcepto(rs.getString("PagoConcepto"));
            pago.setFactura(rs.getString("PagoFactura"));
            return pago;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}