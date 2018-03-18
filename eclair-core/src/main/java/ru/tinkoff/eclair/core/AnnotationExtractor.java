package ru.tinkoff.eclair.core;

import org.springframework.util.ReflectionUtils;
import ru.tinkoff.eclair.annotation.Log;
import ru.tinkoff.eclair.annotation.Mdc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedRepeatableAnnotations;
import static org.springframework.core.annotation.AnnotationUtils.getAnnotationAttributes;
import static org.springframework.core.annotation.AnnotationUtils.synthesizeAnnotation;
import static ru.tinkoff.eclair.core.AnnotationAttribute.LOGGER;

/**
 * @author Viacheslav Klapatniuk
 */
public final class AnnotationExtractor {

    /**
     * In order of supposed popularity
     */
    private static final List<Class<? extends Annotation>> METHOD_TARGET_ANNOTATION_CLASSES = asList(
            Log.class,
            Log.in.class,
            Log.out.class,
            Log.error.class,
            Mdc.class
    );

    /**
     * In order of supposed popularity
     */
    private static final List<Class<? extends Annotation>> PARAMETER_TARGET_ANNOTATION_CLASSES = asList(
            Log.arg.class,
            Mdc.class
    );

    private static final AnnotationExtractor instance = new AnnotationExtractor();

    private AnnotationExtractor() {
    }

    public static AnnotationExtractor getInstance() {
        return instance;
    }

    public Set<Method> getCandidateMethods(Class<?> clazz) {
        return Stream.of(ReflectionUtils.getUniqueDeclaredMethods(clazz))
                .filter(method -> method.getDeclaringClass() != Object.class)
                .filter(method -> !(method.isBridge() || method.isSynthetic()))
                .collect(toSet());
    }

    public boolean hasAnyAnnotation(Method method) {
        return METHOD_TARGET_ANNOTATION_CLASSES.stream()
                .anyMatch(annotationClass -> !findMergedRepeatableAnnotations(method, annotationClass).isEmpty());
    }

    public boolean hasAnyAnnotation(Parameter parameter) {
        return PARAMETER_TARGET_ANNOTATION_CLASSES.stream()
                .anyMatch(annotationClass -> !findMergedRepeatableAnnotations(parameter, annotationClass).isEmpty());
    }

    public Set<Log> getLogs(Method method) {
        return findMergedRepeatableAnnotations(method, Log.class);
    }

    public Set<Log.in> getLogIns(Method method) {
        return findMergedRepeatableAnnotations(method, Log.in.class);
    }

    public Set<Log.out> getLogOuts(Method method) {
        return findMergedRepeatableAnnotations(method, Log.out.class);
    }

    public Set<Log.error> getLogErrors(Method method) {
        return findMergedRepeatableAnnotations(method, Log.error.class);
    }

    public Set<Mdc> getMdcs(Method method) {
        return findMergedRepeatableAnnotations(method, Mdc.class);
    }

    public List<Set<Log.arg>> getLogArgs(Method method) {
        return Stream.of(method.getParameters())
                .map(parameter -> findMergedRepeatableAnnotations(parameter, Log.arg.class))
                .collect(toList());
    }

    public List<Set<Mdc>> getParametersMdcs(Method method) {
        return Stream.of(method.getParameters())
                .map(parameter -> findMergedRepeatableAnnotations(parameter, Mdc.class))
                .collect(toList());
    }

    Log findLog(Method method, Set<String> loggers) {
        return findAnnotation(getLogs(method), loggers);
    }

    Log.in findLogIn(Method method, Set<String> loggers) {
        return findAnnotation(getLogIns(method), loggers);
    }

    Log.out findLogOut(Method method, Set<String> loggers) {
        return findAnnotation(getLogOuts(method), loggers);
    }

    Set<Log.error> findLogErrors(Method method, Set<String> loggers) {
        return findAnnotations(getLogErrors(method), loggers);
    }

    List<Log.arg> findLogArgs(Method method, Set<String> loggers) {
        return getLogArgs(method).stream()
                .map(logArgs -> findAnnotation(logArgs, loggers))
                .collect(toList());
    }

    private <T extends Annotation> T findAnnotation(Collection<T> annotations, Set<?> loggers) {
        return annotations.stream()
                .filter(getLoggerPredicate(loggers))
                .findFirst()
                .orElse(null);
    }

    private <T extends Annotation> Set<T> findAnnotations(Collection<T> annotations, Set<?> loggers) {
        return annotations.stream()
                .filter(getLoggerPredicate(loggers))
                .collect(toCollection(LinkedHashSet::new));
    }

    private <T extends Annotation> Predicate<T> getLoggerPredicate(Set<?> loggers) {
        return annotation -> loggers.contains(LOGGER.extract(annotation));
    }

    Log.in synthesizeLogIn(Log log) {
        return synthesizeAnnotation(getAnnotationAttributes(log), Log.in.class, null);
    }

    Log.out synthesizeLogOut(Log log) {
        return synthesizeAnnotation(getAnnotationAttributes(log), Log.out.class, null);
    }

    Log.arg synthesizeLogArg(Log.in logIn) {
        Map<String, Object> attributes = getAnnotationAttributes(logIn);
        attributes.put("value", logIn.verbose());
        attributes.put("ifEnabled", logIn.verbose());
        return synthesizeAnnotation(attributes, Log.arg.class, null);
    }
}
