package com.guba.app.service.local.database;

import java.sql.ResultSet;

@FunctionalInterface
public interface SQLResult<T>{

    public T convertData(ResultSet t);
}
