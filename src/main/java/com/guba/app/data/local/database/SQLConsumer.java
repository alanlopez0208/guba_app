package com.guba.app.data.local.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLConsumer{
    public void accept(PreparedStatement t) throws SQLException;

}
