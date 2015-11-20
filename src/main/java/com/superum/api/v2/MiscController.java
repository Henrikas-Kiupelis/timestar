package com.superum.api.v2;

import com.superum.api.core.CommonControllerLogic;
import eu.goodlike.time.Time;
import eu.goodlike.time.TimeConverter;
import eu.goodlike.time.TimeHandler;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static java.util.stream.Collectors.toList;

/**
 * Contains various API methods that do not have much to do with the app itself, but are still useful
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/misc")
public class MiscController extends CommonControllerLogic {

    @RequestMapping(value = "/time/zones", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public Set<String> getValidTimeZoneValues() {
        return ZoneId.getAvailableZoneIds();
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
        ZoneId zoneId = time_zone == null ? ZoneId.of("UTC") : ZoneId.of(time_zone);
        LocalDate localDate = date == null ? LocalDate.now(zoneId) : LocalDate.parse(date);
        return Time.at(zoneId).from(localDate, hour, minute, second, millisecond).toEpochMilli();
    }

    @RequestMapping(value = "/datetime/convert", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<String> convert(@RequestParam(value = "time_zone", required = false) String time_zone,
                          @RequestParam(value = "millis", required = false) List<Long> millis) {
        if (millis == null) millis = Collections.singletonList(System.currentTimeMillis());
        ZoneId zoneId = time_zone == null ? ZoneId.of("UTC") : ZoneId.of(time_zone);
        TimeHandler timeHandler = time_zone == null ? Time.atUTC() : Time.at(zoneId);
        return millis.stream().map(timeHandler::from).map(TimeConverter::toDateTime).map(DATE_TIME_FORMAT::format).collect(toList());
    }

    // PRIVATE

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss [zz]");

}
