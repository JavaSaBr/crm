package com.ss.jcrm.web.util;

import org.jetbrains.annotations.NotNull;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

public class TupleUtils {

    public static <A, B, C> @NotNull Tuple3<A, B, C> merge(@NotNull Tuple2<A, B> first, @NotNull C second) {
        return Tuples.of(first.getT1(), first.getT2(), second);
    }
}
