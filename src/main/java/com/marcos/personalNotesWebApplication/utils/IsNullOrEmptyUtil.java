package com.marcos.personalNotesWebApplication.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IsNullOrEmptyUtil {

    public boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public boolean isNullOrEmpty(Object obj) {
        return obj == null;
    }

    public boolean isNullOrEmpty(String[] array) {
        return array == null || array.length == 0;
    }

    public boolean isNullOrEmpty(Integer number) {
        return number == null || number == 0;
    }

    public boolean isNullOrEmpty(Long number) {
        return number == null || number == 0L;
    }

    public boolean isNullOrEmpty(Double number) {
        return number == null || number == 0.0;
    }

    public boolean isNullOrEmpty(Float number) {
        return number == null || number == 0.0f;
    }

    public <T> boolean isNullOrEmpty(List<T> bool) {
        return bool == null || bool.isEmpty();
    }
}
