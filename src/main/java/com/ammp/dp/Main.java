package com.ammp.dp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.run();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void run() throws NoSuchMethodException, NoSuchFieldException {
        AnnotateExample annotateExample = new AnnotateExample();
        annotateExample.example("Artur");

        Method method = annotateExample.getClass().getDeclaredMethod("example", String.class);

        Annotation[][] annotations = method.getParameterAnnotations();
        for (Annotation[] ann :annotations){
            Annotation annotation = ann[0];
            ArgumentA argumentA = (ArgumentA) annotation;
        }

    }
}
