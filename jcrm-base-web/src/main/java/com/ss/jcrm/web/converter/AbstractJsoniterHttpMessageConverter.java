package com.ss.jcrm.web.converter;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.EncodingMode;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.DecodingMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.MimeType;

import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class AbstractJsoniterHttpMessageConverter {

    static {
        JsonIterator.setMode(DecodingMode.DYNAMIC_MODE_AND_MATCH_FIELD_WITH_HASH);
        JsonStream.setMode(EncodingMode.DYNAMIC_MODE);
    }

    protected static final List<MimeType> DEFAULT_MIME_TYPES = List.of(
        new MimeType("application", "json", StandardCharsets.UTF_8),
        new MimeType("application", "*+json", StandardCharsets.UTF_8)
    );

    protected final MimeType[] supportedMimeTypesArray;
    protected final List<MimeType> supportedMimeTypesList;

    protected AbstractJsoniterHttpMessageConverter(@NotNull List<MimeType> supportedMimeTypesList) {
        this.supportedMimeTypesList = supportedMimeTypesList;
        this.supportedMimeTypesArray = supportedMimeTypesList.toArray(MimeType[]::new);
    }

    protected boolean supportsMimeType(@Nullable MimeType mimeType) {

        if (mimeType == null) {
            return true;
        }

        for (var type : supportedMimeTypesArray) {
            if (type.isCompatibleWith(mimeType)) {
                return true;
            }
        }

        return false;
    }
}
