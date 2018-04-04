package ru.tinkoff.eclair.annotation;

import org.springframework.boot.logging.LogLevel;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

import static org.springframework.boot.logging.LogLevel.*;

/**
 * TODO: implement {@link ElementType#TYPE}
 * TODO: add tests of meta-annotations
 *
 * @author Viacheslav Klapatniuk
 */
@Repeatable(Logs.class)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    @AliasFor("level")
    LogLevel value() default DEBUG;

    @AliasFor("value")
    LogLevel level() default DEBUG;

    LogLevel ifEnabled() default OFF;

    LogLevel verbose() default DEBUG;

    String printer() default "";

    /**
     * TODO: convert to logger array everywhere?
     */
    String logger() default "";

    @Repeatable(ins.class)
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface in {

        @AliasFor("level")
        LogLevel value() default DEBUG;

        @AliasFor("value")
        LogLevel level() default DEBUG;

        LogLevel ifEnabled() default OFF;

        LogLevel verbose() default DEBUG;

        String printer() default "";

        String logger() default "";
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface ins {

        @SuppressWarnings("unused")
        in[] value();
    }

    @Repeatable(outs.class)
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface out {

        @AliasFor("level")
        LogLevel value() default DEBUG;

        @AliasFor("value")
        LogLevel level() default DEBUG;

        LogLevel ifEnabled() default OFF;

        LogLevel verbose() default DEBUG;

        String printer() default "";

        String logger() default "";
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface outs {

        @SuppressWarnings("unused")
        out[] value();
    }

    @Repeatable(errors.class)
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface error {

        @AliasFor("level")
        LogLevel value() default ERROR;

        @AliasFor("value")
        LogLevel level() default ERROR;

        LogLevel ifEnabled() default OFF;

        /**
         * TODO: add example for 'verbose' attribute
         */
        LogLevel verbose() default ERROR;

        Class<? extends Throwable>[] ofType() default Throwable.class;

        Class<? extends Throwable>[] exclude() default {};

        String logger() default "";
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface errors {

        @SuppressWarnings("unused")
        error[] value();
    }
}
