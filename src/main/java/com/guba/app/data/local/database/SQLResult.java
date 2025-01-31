package com.guba.app.data.local.database;

import java.sql.ResultSet;

@FunctionalInterface
public interface SQLResult<T>{

    public T convertData(ResultSet t);
}
