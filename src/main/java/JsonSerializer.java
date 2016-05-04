import annotations.CustomDateFormat;
import annotations.JsonValue;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by nastya on 25.04.16.
 */
public class JsonSerializer {

    public String serialize(Object object) {
        StringBuilder value = new StringBuilder("{");
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        boolean isFirstField = true;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!Modifier.isStatic(field.getModifiers())) {
                String fieldName = null;
                Object fieldValue = null;
                field.setAccessible(true);
                Annotation jsonValueAnnotation = field.getAnnotation(JsonValue.class);

                if (jsonValueAnnotation != null) {
                    fieldName = ((JsonValue) jsonValueAnnotation).name();
                } else {
                    fieldName = field.getName();
                }
                String datePattern = null;
                Annotation dateAnnotation = field.getAnnotation(CustomDateFormat.class);
                if(dateAnnotation!=null) {
                    CustomDateFormat customDateFormat = CustomDateFormat.class.cast(dateAnnotation);
                    datePattern = customDateFormat.format();
                }

                try {
                    fieldValue = field.get(object);
                    if (fieldValue != null) {
                        if (!isFirstField) {
                            value.append(",");
                        }
                        isFirstField = false;
                        value.append('"');
                        value.append(fieldName);
                        value.append('"');
                        value.append(":");
                        value.append(getStringValue(fieldValue, datePattern));
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        }
        value.append("}");
        return value.toString();
    }

    private String getStringValue(Object fieldValue, String datePattern) {
        StringBuilder stringValue = new StringBuilder("");
        if (String.class.isAssignableFrom(fieldValue.getClass()) || Character.class.isAssignableFrom(fieldValue.getClass())) {
            stringValue.append('"');
            stringValue.append(fieldValue.toString());
            stringValue.append('"');

        } else if (Collection.class.isAssignableFrom(fieldValue.getClass())) {
            stringValue.append('[');

            Collection collection = Collection.class.cast(fieldValue);
            Iterator iterator = collection.iterator();
            for (int i = 0; i < collection.size(); i++) {
                if (i > 0) {
                    stringValue.append(",");
                }
                stringValue.append(getStringValue(iterator.next(), null));
            }
            stringValue.append(']');
        } else if (fieldValue instanceof Object[]) {
            stringValue.append('[');
            Object[] array = (Object[]) fieldValue;
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    stringValue.append(",");
                }
                stringValue.append(getStringValue(array[i], null));
            }
            stringValue.append(']');
        } else if (Map.class.isAssignableFrom(fieldValue.getClass())) {
            // TODO map serialization
        } else if (Number.class.isAssignableFrom(fieldValue.getClass())) { // primitives
            stringValue.append(fieldValue.toString());
        } else if (Boolean.class.isAssignableFrom(fieldValue.getClass())) {
            stringValue.append(fieldValue.toString());
        } else if (Date.class.isAssignableFrom(fieldValue.getClass())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        } else if (LocalDateTime.class.isAssignableFrom(fieldValue.getClass())) {
            if (datePattern != null) {
                LocalDateTime date = LocalDateTime.class.cast(fieldValue);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                String text = date.format(formatter);
                stringValue.append(text);
            } else {
                stringValue.append(fieldValue.toString());
            }
        } else if (LocalDate.class.isAssignableFrom(fieldValue.getClass())) {
            if (datePattern != null) {

                LocalDate date = LocalDate.class.cast(fieldValue);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                String text = date.format(formatter);
                stringValue.append(text);
            } else {
                stringValue.append(fieldValue.toString());
            }
        } else {
            stringValue.append(serialize(fieldValue));
        }
        // TODO type boolean
        return stringValue.toString();
    }


}