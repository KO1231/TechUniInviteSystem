package org.techuni.TechUniInviteSystem.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GsonUtil {

    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final Gson gson;

    public GsonUtil() {
        gson = (new GsonBuilder()) //
                .registerTypeAdapter(OffsetDateTime.class, offsetDateTimeJsonDeserializer()) //
                .registerTypeAdapter(OffsetDateTime.class, offsetDateTimeJsonSerializer()) //
                .create();
    }

    private static JsonDeserializer<OffsetDateTime> offsetDateTimeJsonDeserializer() {
        return (json, typeOfT, context) -> OffsetDateTime.parse(json.getAsString(), dateTimeFormatter);
    }

    private static JsonSerializer<OffsetDateTime> offsetDateTimeJsonSerializer() {
        return (src, typeOfSrc, context) -> context.serialize(src.format(dateTimeFormatter));
    }
}
