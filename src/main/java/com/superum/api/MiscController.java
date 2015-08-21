package com.superum.api;

import com.superum.api.core.CommonControllerLogic;
import com.superum.helper.time.JodaTimeZoneHandler;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

/**
 * Contains various API methods that do not have much to do with the app itself, but are still useful
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/misc")
public class MiscController extends CommonControllerLogic {

    @RequestMapping(value = "/time/zones", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public Set<String> getValidTimeZoneValues() {
        return DateTimeZone.getAvailableIDs();
    }

    @RequestMapping(value = "/time", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public long getNow() {
        return Instant.now().getMillis();
    }

    @RequestMapping(value = "/time/convert", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public long convert(@RequestParam(value = "timeZone", required = false) String timeZone,
                        @RequestParam(value = "date", required = false) String date,
                        @RequestParam(value = "hour", required = false) Integer hour,
                        @RequestParam(value = "minute", required = false) Integer minute,
                        @RequestParam(value = "second", required = false) Integer second,
                        @RequestParam(value = "millisecond", required = false) Integer millisecond) {
        if (hour == null) hour = 0;
        if (minute == null) minute = 0;
        if (second == null) second = 0;
        if (millisecond == null) millisecond = 0;
        DateTimeZone dateTimeZone = timeZone == null ? JodaTimeZoneHandler.defaultTimeZone() : DateTimeZone.forID(timeZone);
        LocalDate localDate = date == null ? LocalDate.now(dateTimeZone) : LocalDate.parse(date);

        return JodaTimeZoneHandler.forTimeZoneId(timeZone).from(localDate, hour, minute, second, millisecond).toEpochMillis();
    }

}
