package pl.aaugustyniak.myawsomeapp;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 *
 * @author aaugustyniak
 */
@RunWith(ConcurrentTestRunner.class)
public class MyAppTest {

    private static final int CYCLES = 9;
    private static final int THREADS = 100;

    @Rule
    public ConcurrentRule rule = new ConcurrentRule();
    @Rule
    public RepeatingRule repeatedly = new RepeatingRule();

    private final String[] provenWorkDays = {
        "2011-06-13",
        "2011-06-14",
        "2011-06-15",
        "2011-06-16",
        "2011-06-17",
        "2011-06-18"
    };
    private final String[] provenFeasts = {
        "2011-01-01",
        "2011-01-06",
        "2011-04-24",
        "2011-04-25",
        "2011-05-01",
        "2011-05-03",
        "2011-06-12",
        "2011-06-23",
        "2011-11-01",
        "2011-11-11",
        "2011-12-25",
        "2011-12-26",
        "2013-01-01",
        "2013-01-06",
        "2013-03-31",
        "2013-04-01",
        "2013-05-01",
        "2013-05-03",
        "2013-05-19",
        "2013-05-30",
        "2013-11-01",
        "2013-11-11",
        "2013-12-25",
        "2013-12-26"
    };

    private static MyApp mf;

    @BeforeClass
    public static void beforeClass() {
        mf = new MyApp();
    }

    @Concurrent(count = THREADS)
    @Repeating(repetition = CYCLES)
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWithUnparsableString() {
        String s = "#!";
        mf.isFeast(s);
    }

    @Test
    @Concurrent(count = THREADS)
    @Repeating(repetition = CYCLES)
    public void testIsFeastWithString() {
        String s = provenFeasts[0];
        assertTrue(mf.isFeast(s, MyApp.ISO8601_DATE_FORMAT));
    }

    @Test
    @Concurrent(count = THREADS)
    @Repeating(repetition = CYCLES)
    public void testIsFeastWithObject() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(MyApp.ISO8601_DATE_FORMAT);
        DateTime dt = formatter.parseDateTime(provenFeasts[0]);
        Date param = dt.toDate();
        assertTrue(mf.isFeast(param));
    }

    @Test
    @Concurrent(count = THREADS)
    @Repeating(repetition = CYCLES)
    public void testProvenFeasts() {

        for (String day : provenFeasts) {
            assertTrue(mf.isFeast(day, MyApp.ISO8601_DATE_FORMAT));
        }
    }

    @Test
    @Concurrent(count = THREADS)
    @Repeating(repetition = CYCLES)
    public void testProvenWorkDays() {
        for (String day : provenWorkDays) {
            assertFalse(mf.isFeast(day, MyApp.ISO8601_DATE_FORMAT));
        }
    }
}
