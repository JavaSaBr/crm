package com.ss.jcrm.web.converter;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import com.ss.jcrm.base.utils.CommonUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.converter.json.AbstractJsonHttpMessageConverter;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JsoniterHttpMessageConverter extends AbstractJsonHttpMessageConverter {

    static {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
    }

    @Override
    protected @Nullable Object readInternal(Type resolvedType, Reader reader) throws Exception {
        return JsonIterator.parse(CommonUtils.toString(reader))
            .read(resolvedType);
    }

    @Override
    protected void writeInternal(@NotNull Object object, @Nullable Type type, @NotNull Writer writer) throws Exception {
        if (type instanceof ParameterizedType) {
            writer.write(JsonStream.serialize(false, type, object));
        } else {
            writer.write(JsonStream.serialize(object));
        }
    }
}
