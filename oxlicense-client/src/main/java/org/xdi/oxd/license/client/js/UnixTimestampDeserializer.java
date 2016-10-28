package org.xdi.oxd.license.client.js;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * @author Yuriy Zabrovarnyy
 * @version 0.9, 18/10/2016
 */

public class UnixTimestampDeserializer extends JsonDeserializer<Date> {
    private final static Logger LOG = LoggerFactory.getLogger(UnixTimestampDeserializer.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String date = jp.getText().trim();

        try {
            if (date.length() == 10) {
                date = date + "000";
            }
            return new Date(Long.valueOf(date));
        } catch (Exception e) {
            LOG.error("Unable to deserialize date: " + date, e);
            return null;
        }
    }
}