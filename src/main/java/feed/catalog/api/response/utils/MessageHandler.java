package feed.catalog.api.response.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
public class MessageHandler {

    Set<String> primitive;
    Map<String,Class> udt;
}
