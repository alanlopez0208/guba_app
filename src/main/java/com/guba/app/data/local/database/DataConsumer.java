package com.guba.app.data.local.database;

import com.guba.app.data.local.database.conexion.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataConsumer<T>{
    private Connection connection;
    public DataConsumer(){
        connection = Conexion.getConection();
    }

    public List<T> getList(String sql, SQLConsumer preparaed, SQLResult<T> mapper){
        List<T> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            preparaed.accept(pstmt);
            ResultSet resultSet = pstmt.executeQuery();


            while (resultSet.next()){
                list.add(mapper.convertData(resultSet));
            }

        }catch (SQLException e){
            System.out.println("Error: "+ e);
        }
        return list;
    }

    public List<T> getList(String sql, SQLResult<T> mapper){
        List<T> list = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()){

                list.add(mapper.convertData(resultSet));
            }

        }catch (SQLException e){
            System.out.println("Error "+ e);
        }
        return list;
    }

    public T getData(String sql, SQLConsumer consumer, SQLResult<T> mapper){
        T data = null;
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            consumer.accept(pstmt);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()){
                data = mapper.convertData(resultSet);
            }
        }catch (SQLException e){
            System.out.println("Error "+ e);
        }
        return data;
    }


    public T getData(String sql ,SQLResult<T> mapper){
        T data = null;
        try {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            ResultSet resultSet = prepareStatement.executeQuery();
            if (resultSet.next()){
                data = mapper.convertData(resultSet);
            }
        }catch (SQLException e){
            System.out.println("Error "+ e);
        }
        return data;
    }

    public <T> Optional<T> executeUpdate(String sql, SQLConsumer consumer, boolean returnGeneratedKeys) {
        try (PreparedStatement pt = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS)) {

            if (consumer != null) {
                consumer.accept(pt);
            }
            int affectedRows = pt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La operación no afectó ninguna fila.");
            }
            if (returnGeneratedKeys) {
                try (ResultSet generatedKeys = pt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {

                        T key = (T) Integer.valueOf(generatedKeys.getInt(1));
                        return Optional.of(key);
                    } else {
                        throw new SQLException("La operación no generó ninguna clave.");
                    }
                }
            }

            T success = (T) Boolean.TRUE;
            return Optional.of(success);

        } catch (SQLException e) {
            System.err.println("Error en la consulta: " + e.getMessage());
            return Optional.empty();
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
        try {
            connection.setAutoCommit(false);
            E data = operation.execute(connection);
            connection.commit();
            return Optional.of(data);
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Transacción revertida: " + e.getMessage());
                return Optional.empty();
            } catch (SQLException rollbackEx) {
                System.out.println("Error al realizar rollback: " + rollbackEx.getMessage());
                return Optional.empty();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al reiniciar AutoCommit: " + e.getMessage());
               return Optional.empty();
            }
        }
    }

    public boolean excuteBatch(String sql, SQLConsumer consumer){
        boolean success = false;
        try {
            connection.setAutoCommit(false);
            PreparedStatement pt = connection.prepareStatement(sql);
            consumer.accept(pt);
            int[] results = pt.executeBatch();

            for (int result : results) {
                success = result > 0;
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Transacción revertida: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                System.out.println("Error al realizar rollback: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error al reiniciar AutoCommit: " + e.getMessage());
            }
        }
        return success;
    }
}

