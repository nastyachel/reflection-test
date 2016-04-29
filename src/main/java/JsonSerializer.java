import annotations.JsonValue;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by nastya on 25.04.16.
 */
public class JsonSerializer {

    public String serialize(Object object) {
        StringBuilder value = new StringBuilder("{");
        Class objectClass = object.getClass();
        Field[] fields = objectClass.getDeclaredFields();
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

                try {
                    fieldValue = field.get(object);
                    if (fieldValue != null) {
                        if (i > 0) {
                            value.append(",");
                        }
                        value.append('"');
                        value.append(fieldName);
                        value.append('"');
                        value.append(":");
                        value.append(getStringValue(fieldValue));
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }


            }
        }
        value.append("}");
        return value.toString();
    }

    private String getStringValue(Object fieldValue) {
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
                stringValue.append(getStringValue(iterator.next()));
            }
            stringValue.append(']');
        } else if (Arrays.class.isAssignableFrom(fieldValue.getClass())) {
            stringValue.append('[');
            Object[] array = (Object[]) fieldValue;
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    stringValue.append(",");
                }
                stringValue.append(getStringValue(array[i]));
            }
            stringValue.append(']');
        } else if (Map.class.isAssignableFrom(fieldValue.getClass())) {
            // TODO map serialization
        } else if (Number.class.isAssignableFrom(fieldValue.getClass())) { // primitives
            stringValue.append(fieldValue.toString());
        } else if (Boolean.class.isAssignableFrom(fieldValue.getClass())) {
            stringValue.append(fieldValue.toString());
        } else {
            stringValue.append(serialize(fieldValue));
        }
        // TODO type boolean
        return stringValue.toString();
    }


}