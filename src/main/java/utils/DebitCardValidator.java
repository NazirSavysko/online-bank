package utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public final class DebitCardValidator implements Validator {

    @Override
    public boolean supports(final @NotNull Class<?> clazz) {
        return Object.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final @NotNull Object target, final @NotNull Errors errors) {
        final Class<?> clazz = target.getClass();

        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            this.validateField(field, target, errors);
        }

    }

    private void validateField(final Field field,final Object target,final Errors errors) {
        field.setAccessible(true);
        try {
            Annotation[] annotation = field.getAnnotations();
            for (Annotation a : annotation) {
                if (a.annotationType().getSimpleName().equals("NotBlank")) {
                    Object value = field.get(target);
                    String message = ((NotBlank) a).message();
                    if (value == null || (value instanceof String && ((String) value).isBlank())) {
                        errors.rejectValue(field.getName(), "field.notBlank", message);
                    }
                } else if (a.annotationType().getSimpleName().equals("Pattern")) {
                    Object value = field.get(target);
                    if (value instanceof String) {
                        String strValue = value.toString();
                        String regex = ((Pattern) a).regexp();
                        String message = ((Pattern) a).message();
                        if (!strValue.matches(regex)) {
                            errors.rejectValue(field.getName(), "field.invalidFormat", message);
                        }
                    }
                } else if (a.annotationType().getSimpleName().equals("NotNull")) {
                    Object value = field.get(target);
                    if (value == null) {
                        errors.rejectValue(field.getName(), "field.notNull", field.getName() + " cannot be null");
                    }
                }
            }
        } catch (Exception e) {
            errors.reject("field.accessError", "Cannot access field: " + field.getName());
        }
    }
}
