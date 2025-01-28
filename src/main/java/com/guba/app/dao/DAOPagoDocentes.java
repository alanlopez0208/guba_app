package com.guba.app.dao;

import com.guba.app.service.local.database.DataConsumer;
import com.guba.app.models.Maestro;
import com.guba.app.models.PagoDocente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DAOPagoDocentes {

    private DataConsumer<PagoDocente> dataConsumer = new DataConsumer<>();

    final String SELECT_ALL = """
                    SELECT
                    p.IdPago AS PagoIdPago,
                    p.Fecha AS PagoFecha,
                	p.Cantidad AS PagoCantidad,
                	p.Concepto AS PagoConcepto,
                	p.Factura AS PagoFactura,
                	c.IdDocente AS DocenteId,
                	c.RFC AS DocenteRFC,
                	c.Nombre AS DocenteNombre,
                    c.ApellidoPaterno AS DocenteApellidoPaterno,
                	c.ApellidoMaterno AS DocenteApellidoMaterno
                FROM PagoDocentes AS p
                INNER JOIN Docentes AS c ON p.IdDocente = c.IdDocente
            """;

    public List<PagoDocente> getPagos() {
        return dataConsumer.getList(SELECT_ALL, this::mapResultSetToPago);
    }


    public List<PagoDocente> buscarPagos(String where, String filtro) {
        String sql = SELECT_ALL+ " WHERE " + where + " LIKE ?";

        return dataConsumer.getList(sql, pstmt->{
            pstmt.setString(1, filtro + "%");
        },this::mapResultSetToPago);
    }

    public boolean updatePago(PagoDocente pagoModelo) {
        String sql = "UPDATE PagoDocentes SET IdDocente = ?, Fecha = ?, Cantidad = ?, Concepto = ?, Factura = ? WHERE IdPago = ?";

        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, pagoModelo.getMaestro().getId());
            pstmt.setString(2, pagoModelo.getFecha());
            pstmt.setString(3, pagoModelo.getCantidad());
            pstmt.setString(4, pagoModelo.getConcepto());
            pstmt.setString(5,pagoModelo.getFactura());
            pstmt.setString(6, pagoModelo.getIdPago());
        });
    }


    public boolean deletePago(String idPago) {
        String sql = "DELETE FROM PagoDocentes WHERE IdPago = ?";
        return dataConsumer.executeUpdate(sql, pstmt->{
            pstmt.setString(1, idPago);
        });
    }


    public PagoDocente retornarPago(String idPago) {
        String sql = SELECT_ALL+" WHERE IdPago = ?";
        return dataConsumer.getData(sql, pstmt->{
            pstmt.setString(1, idPago);
        },this::mapResultSetToPago);
    }


    public Optional<Integer> crearPago(PagoDocente pagoModelo) {
        String sql = "INSERT INTO PagoDocentes (IdPago, IdDocente, Fecha, Cantidad, Concepto, Factura) VALUES (?, ?, ?, ?, ?, ?)";

        return dataConsumer.executeUpdateWithGeneratedKeys(sql,pstmt->{
            pstmt.setString(1, pagoModelo.getIdPago());
            pstmt.setString(2, pagoModelo.getMaestro().getId());
            pstmt.setString(3, pagoModelo.toStringDate());
            pstmt.setString(4, pagoModelo.getCantidad());
            pstmt.setString(5, pagoModelo.getConcepto());
            pstmt.setString(6, pagoModelo.getFactura());
        });
    }

    private PagoDocente mapResultSetToPago(ResultSet rs){
        try {
            PagoDocente pago = new PagoDocente();
            pago.setMaestro(new Maestro());
            pago.getMaestro().setId(rs.getString("DocenteId"));
            pago.getMaestro().setRfc(rs.getString("DocenteRFC"));
            pago.getMaestro().setNombre(rs.getString("DocenteNombre"));
            pago.getMaestro().setApPat(rs.getString("DocenteApellidoPaterno"));
            pago.getMaestro().setApMat(rs.getString("DocenteApellidoMaterno"));
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
