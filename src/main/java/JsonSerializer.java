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
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
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
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

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
            if (dateAnnotation != null) {
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

            int objectCounter = 0;
            for (Object object : Collection.class.cast(fieldValue)) {
                if (objectCounter > 0) {
                    stringValue.append(",");
                }
                objectCounter++;
                stringValue.append(getStringValue(object, null));
            }
            stringValue.append(']');
        } else if (Object[].class.isAssignableFrom(fieldValue.getClass())){
            stringValue.append('[');
            int objectCounter = 0;
            for (Object object : (Object[]) fieldValue) {
                if (objectCounter > 0) {
                    stringValue.append(",");
                }
                objectCounter++;
                stringValue.append(getStringValue(object, null));
            }
            stringValue.append(']');
        } else if (Map.class.isAssignableFrom(fieldValue.getClass())) {
            int objectCounter = 0;
            for (Object entryObject : Map.class.cast(fieldValue).entrySet()) {
                Map.Entry entry = (Map.Entry) entryObject;
                if (objectCounter > 0) {
                    stringValue.append(",");
                }
                objectCounter++;
                stringValue.append(entry.getKey());
                stringValue.append(":");
                stringValue.append(getStringValue(entry.getValue(), null));
            }
            stringValue.append(']');

        } else if (Number.class.isAssignableFrom(fieldValue.getClass())||Boolean.class.isAssignableFrom(fieldValue.getClass())) { // primitives
            stringValue.append(fieldValue.toString());
        } else if (Date.class.isAssignableFrom(fieldValue.getClass())) {
            if (datePattern != null) {
                Date date = Date.class.cast(fieldValue);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                String text = simpleDateFormat.format(date);
                stringValue.append(text);
            } else {
                stringValue.append(fieldValue.toString());
            }
        } else if (TemporalAccessor.class.isAssignableFrom(fieldValue.getClass())) {
            if (datePattern != null) {
                TemporalAccessor date = TemporalAccessor.class.cast(fieldValue);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
                stringValue.append(formatter.format(date));
            } else {
                stringValue.append(fieldValue.toString());
            }
        } else {
            stringValue.append(serialize(fieldValue));
        }
        return stringValue.toString();
    }

}