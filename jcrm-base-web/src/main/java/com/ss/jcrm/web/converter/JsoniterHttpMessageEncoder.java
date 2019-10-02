package com.ss.jcrm.web.converter;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageEncoder;
import org.springframework.util.MimeType;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public class JsoniterHttpMessageEncoder extends AbstractJsoniterHttpMessageConverter implements HttpMessageEncoder<Object> {

    protected JsoniterHttpMessageEncoder(@NotNull List<MimeType> supportedMimeTypesList) {
        super(supportedMimeTypesList);
    }

    @Override
    public boolean canEncode(ResolvableType elementType, MimeType mimeType) {
        return false;
    }

    @Override
    public Flux<DataBuffer> encode(
        Publisher<?> inputStream,
        DataBufferFactory bufferFactory,
        ResolvableType elementType,
        MimeType mimeType,
        Map<String, Object> hints
    ) {
        return null;
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return null;
    }

    @Override
    public List<MediaType> getStreamingMediaTypes() {
        return null;
    }

    /* private static final byte[] NEWLINE_SEPARATOR = {'\n'};

    private static final Map<MediaType, byte[]> STREAM_SEPARATORS;

    static {
        STREAM_SEPARATORS = new HashMap<>();
        STREAM_SEPARATORS.put(MediaType.APPLICATION_STREAM_JSON, NEWLINE_SEPARATOR);
        STREAM_SEPARATORS.put(MediaType.parseMediaType("application/stream+x-jackson-smile"), new byte[0]);
    }

    private final List<MediaType> streamingMediaTypes;

    public JsoniterHttpMessageEncoder() {
        super(DEFAULT_MIME_TYPES);
        this.streamingMediaTypes = List.of(MediaType.APPLICATION_STREAM_JSON);
    }

    @Override
    public boolean canEncode(@NotNull ResolvableType elementType, @Nullable MimeType mimeType) {
        return supportsMimeType(mimeType);
    }

    @Override
    public @NotNull Flux<DataBuffer> encode(
        @NotNull Publisher<?> inputStream,
        @NotNull DataBufferFactory bufferFactory,
        @NotNull ResolvableType elementType,
        @Nullable MimeType mimeType,
        @Nullable Map<String, Object> hints
    ) {

        if (inputStream instanceof Mono) {
            return Mono.from(inputStream)
                .map(value -> encodeValue(value, bufferFactory, elementType))
                .flux();
        } else {
            return streamingMediaTypes.stream()
                .filter(mediaType -> mediaType.isCompatibleWith(mimeType))
                .findFirst()
                .map(mediaType -> {
                    var separator = STREAM_SEPARATORS.getOrDefault(mediaType, NEWLINE_SEPARATOR);
                    return Flux.from(inputStream)
                        .map(value -> {
                            var buffer = encodeValue(value, bufferFactory, elementType);
                            if (separator != null) {
                                buffer.write(separator);
                            }
                            return buffer;
                        });
                })
                .orElseGet(() -> {
                    var listType = ResolvableType.forClassWithGenerics(List.class, elementType);
                    return Flux.from(inputStream)
                        .collectList()
                        .map(list -> encodeValue(list, bufferFactory, listType))
                        .flux();
                });
        }
    }

    private @NotNull DataBuffer encodeValue(
        @NotNull Object value,
        @NotNull DataBufferFactory bufferFactory,
        @NotNull ResolvableType elementType
    ) {

        var buffer = bufferFactory.allocateBuffer();
        var outputStream = buffer.asOutputStream();

        var release = true;
        try {
            JsonStream.serialize(elementType.getType(), value, outputStream);
            release = false;
        } finally {
            if (release) {
                DataBufferUtils.release(buffer);
            }
        }

        return buffer;
    }

    @Override
    public List<MimeType> getEncodableMimeTypes() {
        return supportedMimeTypesList;
    }

    @Override
    public List<MediaType> getStreamingMediaTypes() {
        return streamingMediaTypes;
    }

     */
}
