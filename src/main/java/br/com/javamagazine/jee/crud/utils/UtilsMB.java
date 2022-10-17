package br.com.javamagazine.jee.crud.utils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.TimeZone;

@Named
@ApplicationScoped
public class UtilsMB {

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}