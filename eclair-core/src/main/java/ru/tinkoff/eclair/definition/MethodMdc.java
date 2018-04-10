/*
 * Copyright 2018 Tinkoff Bank
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.tinkoff.eclair.definition;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * @author Vyacheslav Klapatnyuk
 */
@Getter
@Builder
public class MethodMdc {

    @NonNull
    private Method method;

    @Getter
    @NonNull
    @Singular
    private List<String> parameterNames;

    @NonNull
    @Singular
    private Set<ParameterMdc> methodDefinitions;

    @NonNull
    @Singular
    private List<Set<ParameterMdc>> parameterDefinitions;
}
