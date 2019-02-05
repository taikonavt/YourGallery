package com.example.maxim.imageviewer.mvp.model.preferences;

import com.example.maxim.imageviewer.common.Theme;

public interface IPrefs {
    Theme getTheme();

    void setTheme(Theme theme);
}
