package com.ss.crm.db.convert;

import javax.persistence.AttributeConverter;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * @author JavaSaBr
 */
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(final ZonedDateTime dateTime) {
        return Timestamp.from(dateTime.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(final Timestamp date) {
        return ZonedDateTime.from(date.toInstant());
    }
}
