package com.example.maxim.imageviewer.mvp.model.entity;

public class Envelop {
    private Type type;
    private Object data;

    public Envelop(Type type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public Object getData() {
        return data;
    }


    public enum Type {
        LIST, MESSAGE
    }
}
