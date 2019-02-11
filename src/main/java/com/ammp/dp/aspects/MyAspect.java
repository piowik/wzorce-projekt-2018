package com.ammp.dp.aspects;

import com.ammp.dp.Interface;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MyAspect {

    void metoda(Interface i){
        Interface anInterface = i;
    }

}
