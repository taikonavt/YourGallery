package com.example.maxim.imageviewer.mvp.model.preferences;

import android.content.Context;

import com.example.maxim.imageviewer.App;
import com.example.maxim.imageviewer.common.Theme;

public class SharedPreferences implements IPrefs {

    private static final String THEME = "theme";
    private static final int GREEN = 1;
    private static final int BLACK = 2;


    @Override
    public Theme getTheme() {
        int flag = App.getInstance().getSharedPreferences(THEME, Context.MODE_PRIVATE).getInt(THEME, GREEN);
        if (flag == GREEN){
            return Theme.Green;
        } else if (flag == BLACK) {
            return Theme.Black;
        } else throw new RuntimeException("Wrong theme preferences");
    }

    @Override
    public void setTheme(Theme theme) {
        int flag;
        if (theme == Theme.Green) {
            flag = GREEN;
        } else {
            flag = BLACK;
        }
        App.getInstance().getSharedPreferences(THEME, Context.MODE_PRIVATE)
                .edit()
                .putInt(THEME, flag)
                .apply();
    }
}
