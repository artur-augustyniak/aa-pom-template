package pl.aaugustyniak.myawsomeapp;

import com.google.code.tempusfugit.concurrency.annotations.GuardedBy;
import static com.google.code.tempusfugit.concurrency.annotations.GuardedBy.Type.ITSELF;
import com.google.code.tempusfugit.concurrency.annotations.ThreadSafe;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @TODO https://docs.oracle.com/javase/tutorial/i18n/index.html
 * http://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
 * http://www.oracle.com/us/technologies/java/locale-140624.html
 * http://stackoverflow.com/questions/9282419/how-can-i-get-the-number-of-days-in-a-year-using-jodatime
 * http://stackoverflow.com/questions/1339351/java-date-format-for-locale
 * @author aaugustyniak
 */
@ThreadSafe
public class MyApp {

    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd";

    @GuardedBy(lock = ITSELF)
    private final Locale locale;

    /**
     * Default constructor. Set locale to current system default.
     */
    public MyApp() {
        this(Locale.getDefault());
    }

    /**
     * @param l
     */
    public MyApp(Locale l) {
        locale = l;

    }

    /**
     * Check if given day is official feast in Poland
     *
     * @param day
     * @return
     */
    public boolean isFeast(DateTime day) {

        int year = day.getYear();
        int leapYearStep = (isLeap(year)) ? 1 : 0;
        int easterFirstDay = findFirstEasterDayIn(year);
        Map<Integer, String> feastDays = new HashMap<>();
        feastDays.put(1, "Nowy Rok");
        feastDays.put(6, "Trzech Króli");
        feastDays.put(easterFirstDay, "Niedziela Wielkanocna");
        feastDays.put(1 + easterFirstDay, "Poniedziałek Wielkanocny");
        feastDays.put(121 + leapYearStep, "święto Pracy");
        feastDays.put(123 + leapYearStep, "Trzeciego Maja");
        feastDays.put(49 + easterFirstDay, "Zesłanie Ducha Świętego");
        feastDays.put(60 + easterFirstDay, "Boże Ciało");
        feastDays.put(305 + leapYearStep, "Wszystkich Świętych");
        feastDays.put(315 + leapYearStep, "Święto niepodległości");
        feastDays.put(359 + leapYearStep, "Boże Narodzenie I");
        feastDays.put(360 + leapYearStep, "Boże Narodzenie II");
        int dayOfYear = day.getDayOfYear();
        return feastDays.containsKey(dayOfYear);
    }

    /**
     * Check if given day is official feast in Poland parse date string with
     * given format
     *
     * @param day
     * @param datePattern
     * @return
     */
    public boolean isFeast(String day, String datePattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(datePattern);
        return isFeast(formatter.parseDateTime(day));
    }

    /**
     * @see #isFest(java.lang.String, java.lang.String)
     * @param day
     * @return
     */
    public boolean isFeast(String day) {
        return isFeast(day, DateTimeFormat.patternForStyle("S-", locale));
    }

    /**
     * @see #isFeast(org.joda.time.DateTime)
     *
     * @param day
     * @return
     */
    public boolean isFeast(Date day) {
        DateTime dt = new DateTime(day);
        return isFeast(dt);
    }

    private int findFirstEasterDayIn(int year) {
        int leapYearStep = (isLeap(year)) ? 1 : 0;
        int w1 = year % 19;
        int w2 = year / 100;
        int w3 = year % 100;
        int w4 = w2 / 4;
        int w5 = w2 % 4;
        int w6 = (w2 + 8) / 25;
        int w7 = (w2 - w6 + 1) / 3;
        int w8 = (19 * w1 + w2 - w4 - w7 + 15) % 30;
        int w9 = w3 / 4;
        int w10 = w3 % 4;
        int w11 = (32 + 2 * w5 + 2 * w9 - w8 - w10) % 7;
        int w12 = (w1 + 11 * w8 + 22 * w11) / 451;
        int w13 = (w8 + w11 - 7 * w12 + 114);
        int dayNymber = -33 + w13 + leapYearStep;
        return dayNymber;
    }

    private boolean isLeap(int year) {
        boolean isMod4 = (year % 4) != 0;
        boolean isMod100 = (year % 100) != 0;
        boolean isMod400 = (year % 400) != 0;
        return !(isMod4) && ((isMod100) || !(isMod400));
    }

}
