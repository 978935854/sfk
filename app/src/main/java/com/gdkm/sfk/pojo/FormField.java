package com.gdkm.sfk.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/15
  */
public class FormField implements Serializable {
    private String name;
    private Object value;

    public FormField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {

        return name;
    }

    public Object getValue() {
        return value;
    }
}
