package com.guba.app.presentation.utils;

import javafx.scene.control.ListCell;

public class ComboCell<T extends ComboBoxCell> extends ListCell<T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty){
            this.setText(null);
        }else{
            this.setText(item.toComboCell());
        }
    }
}
