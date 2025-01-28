package com.guba.app.controllers;

public interface Loadable<T> {
    void loadData(T data);
}
