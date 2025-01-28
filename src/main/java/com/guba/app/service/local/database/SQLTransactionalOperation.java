package com.guba.app.service.local.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLTransactionalOperation<T> {
    T execute(Connection connection) throws SQLException;
}
