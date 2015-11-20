package com.superum.api.v2;

import com.superum.api.core.CommonControllerLogic;
import eu.goodlike.libraries.joda.time.Time;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.Set;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

/**
 * Contains various API methods that do not have much to do with the app itself, but are still useful
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/misc")
public class MiscController extends CommonControllerLogic {

    @RequestMapping(value = "/time/zones", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public Set<String> getValidTimeZoneValues(@RequestParam(value = "type") String type) {
        if (type == null) type = "";
        return type.toUpperCase().startsWith("JODA")
                ? DateTimeZone.getAvailableIDs()
                : ZoneId.getAvailableZoneIds();
    }

    @RequestMapping(value = "/time", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public long getNow() {
        return System.currentTimeMillis();
    }

    @RequestMapping(value = "/time/convert", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public long convert(@RequestParam(value = "time_zone", required = false) String time_zone,
                        @RequestParam(value = "date", required = false) String date,
                        @RequestParam(value = "hour", required = false) Integer hour,
                        @RequestParam(value = "minute", required = false) Integer minute,
                        @RequestParam(value = "second", required = false) Integer second,
                        @RequestParam(value = "millisecond", required = false) Integer millisecond) {
        if (hour == null) hour = 0;
        if (minute == null) minute = 0;
        if (second == null) second = 0;
        if (millisecond == null) millisecond = 0;
        DateTimeZone dateTimeZone = time_zone == null ? Time.defaultTimeZone() : DateTimeZone.forID(time_zone);
        LocalDate localDate = date == null ? LocalDate.now(dateTimeZone) : LocalDate.parse(date);

        return Time.forZone(dateTimeZone).from(localDate, hour, minute, second, millisecond).toEpochMillis();
    }

    @RequestMapping(value = "/datetime/convert", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public String convert(@RequestParam(value = "time_zone", required = false) String time_zone,
                        @RequestParam(value = "millis", required = false) Long millis) {
        if (millis == null) millis = System.currentTimeMillis();
        DateTimeZone dateTimeZone = time_zone == null ? Time.defaultTimeZone() : DateTimeZone.forID(time_zone);
        return Time.forZone(dateTimeZone).from(millis).toJodaDateTime().toString();
    }

}
