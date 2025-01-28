package com.guba.app.controllers;

import java.util.List;

public interface Mediador<T>  {
    void changePane(Paginas paginas);
    void loadData(Paginas pagina,T c);
    List<T> loadData();
}
