package com.validator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AoS on 2019/2/22 0022.
 */

public class ClassContent {

    private Class clz;

    private HashMap<String, Field> fields = new HashMap<>();
    private ArrayList<Method> methods = new ArrayList<>();

    public ClassContent(Class clz) {
        this.clz = clz;
    }

    public void addField(String name, Field field) {
        fields.put(name, field);
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, Field> fields) {
        this.fields = fields;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }
}
