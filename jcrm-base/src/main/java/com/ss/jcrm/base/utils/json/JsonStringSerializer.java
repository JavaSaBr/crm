package com.ss.jcrm.base.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.ss.rlib.common.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class JsonStringSerializer {

    public static class Serializer extends JsonSerializer<String> {

        @Override
        public boolean isEmpty(@NotNull SerializerProvider provider, @Nullable String value) {
            return value == null || value.isEmpty();
        }

        @Override
        public void serialize(
            @Nullable String value, @NotNull JsonGenerator generator, @NotNull SerializerProvider serializers
        ) throws IOException {
            if (value == null) {
                generator.writeNull();
            } else {
                generator.writeString(encode(value));
            }
        }

        @Override
        public void serializeWithType(
            @Nullable String value,
            @NotNull JsonGenerator generator,
            @NotNull SerializerProvider serializers,
            @NotNull TypeSerializer typeSerializer
        ) throws IOException {
            serialize(value, generator, serializers);
        }
    }

    public static class Deserializer extends JsonDeserializer<String> {

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override // since 2.9
        public @NotNull Object getEmptyValue(@NotNull DeserializationContext deserializationContext) {
            return "";
        }

        @Override
        public @NotNull String deserialize(
            @NotNull JsonParser parser,
            @NotNull DeserializationContext deserializationContext
        ) throws IOException {

            if (parser.hasToken(JsonToken.VALUE_STRING)) {
                return decode(parser.getText());
            }

            return (String) deserializationContext.handleUnexpectedToken(String.class, parser);
        }

        @Override
        public @NotNull String deserializeWithType(
            @NotNull JsonParser parser,
            @NotNull DeserializationContext context,
            @NotNull TypeDeserializer typeDeserializer
        ) throws IOException {
            return deserialize(parser, context);
        }
    }

    private static @NotNull String encode(@NotNull String value) {
        return value.replace("+", "\\+");
    }

    private static @NotNull String decode(@NotNull String value) {
        return value.replace("\\+", "+");
    }
}
