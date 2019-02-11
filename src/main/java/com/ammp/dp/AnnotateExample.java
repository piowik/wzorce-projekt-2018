package com.ammp.dp;

public class AnnotateExample {

    @MyAnnotation
    public String example(@ArgumentA String elo){
        return elo.toUpperCase();
    }
}
