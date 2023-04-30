package svobodavlad.util;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtil {

    public Instant getCurrentDateTime() {
        return Instant.now();
    }
}
