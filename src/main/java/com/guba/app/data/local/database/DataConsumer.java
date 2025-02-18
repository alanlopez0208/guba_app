package com.guba.app.data.local.database;

import com.guba.app.data.local.database.conexion.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class DataConsumer<T> {

    public List<T> getList(String sql, SQLConsumer preparaed, SQLResult<T> mapper) {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = Conexion.getConnection();
            pstmt = connection.prepareStatement(sql);
            preparaed.accept(pstmt);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapper.convertData(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        } finally {
            closeResources(resultSet, pstmt, connection);
        }
        return list;
    }

    public List<T> getList(String sql, SQLResult<T> mapper) {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = Conexion.getConnection();
            pstmt = connection.prepareStatement(sql);
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                list.add(mapper.convertData(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error " + e);
        } finally {
            closeResources(resultSet, pstmt, connection);
        }
        return list;
    }

    public T getData(String sql, SQLConsumer consumer, SQLResult<T> mapper) {
        T data = null;
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = Conexion.getConnection();
            pstmt = connection.prepareStatement(sql);
            consumer.accept(pstmt);
            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                data = mapper.convertData(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        } finally {
            closeResources(resultSet, pstmt, connection);
        }
        return data;
    }

    public T getData(String sql, SQLResult<T> mapper) {
        T data = null;
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Conexion.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                data = mapper.convertData(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
        } finally {
            closeResources(resultSet, prepareStatement, connection);
        }
        return data;
    }

    public <T> Optional<T> executeUpdate(String sql, SQLConsumer consumer, boolean returnGeneratedKeys) {
        Connection connection = null;
        PreparedStatement pt = null;
        ResultSet generatedKeys = null;
        try {
            connection = Conexion.getConnection();
            pt = connection.prepareStatement(sql,
                    returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);

            if (consumer != null) {
                consumer.accept(pt);
            }
            int affectedRows = pt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La operación no afectó ninguna fila.");
            }
            if (returnGeneratedKeys) {
                generatedKeys = pt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    T key = (T) Integer.valueOf(generatedKeys.getInt(1));
                    return Optional.of(key);
                } else {
                    throw new SQLException("La operación no generó ninguna clave.");
                }
            }

            T success = (T) Boolean.TRUE;
            return Optional.of(success);

        } catch (SQLException e) {
            System.err.println("Error en la consulta: " + e.getMessage());
            return Optional.empty();
        } finally {
            closeResources(generatedKeys, pt, connection);
        }
    }

    public boolean executeUpdate(String sql) {
        return (boolean) executeUpdate(sql, null, false).orElse(false);
    }

    public Optional<Integer> executeUpdateWithGeneratedKeys(String sql, SQLConsumer consumer) {
        return executeUpdate(sql, consumer, true);
    }

    public boolean executeUpdate(String sql, SQLConsumer consumer) {
        return (boolean) executeUpdate(sql, consumer, false).orElse(false);
    }

    public <E> Optional<E> executeTransaction(SQLTransactionalOperation<E> operation) {
        Connection connection = null;
        try {
            connection = Conexion.getConnection();
            connection.setAutoCommit(false);
            E data = operation.execute(connection);
            connection.commit();
            return Optional.of(data);
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("Transacción revertida: " + e.getMessage());
                return Optional.empty();
            } catch (SQLException rollbackEx) {
                System.out.println("Error al realizar rollback: " + rollbackEx.getMessage());
                return Optional.empty();
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error al reiniciar AutoCommit: " + e.getMessage());
                }
            }
        }
    }

    public boolean excuteBatch(String sql, SQLConsumer consumer) {
        boolean success = false;
        Connection connection = null;
        PreparedStatement pt = null;
        try {
            connection = Conexion.getConnection();
            connection.setAutoCommit(false);
            pt = connection.prepareStatement(sql);
            consumer.accept(pt);
            int[] results = pt.executeBatch();

            for (int result : results) {
                success = result > 0;
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
                System.out.println("Transacción revertida: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Error al realizar rollback: " + rollbackEx.getMessage());
            }
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error al reiniciar AutoCommit: " + e.getMessage());
                }
            }
            if (pt != null) {
                try {
                    pt.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar PreparedStatement: " + e.getMessage());
                }
            }
        }
        return success;
    }

    private void closeResources(ResultSet resultSet, PreparedStatement pstmt, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar ResultSet: " + e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar PreparedStatement: " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error al cerrar Connection: " + e.getMessage());
            }
        }
    }
}