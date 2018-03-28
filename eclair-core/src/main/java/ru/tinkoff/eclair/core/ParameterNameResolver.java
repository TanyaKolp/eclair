package ru.tinkoff.eclair.core;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.Objects.isNull;

/**
 * @author Viacheslav Klapatniuk
 */
public final class ParameterNameResolver {

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public List<String> tryToResolve(Method method) {
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        if (isNull(parameterNames)) {
            return nCopies(method.getParameterCount(), null);
        }
        return asList(parameterNames);
    }

    void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }
}
