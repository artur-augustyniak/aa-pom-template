package pl.aaugustyniak.myawsomeapp;

import com.google.code.tempusfugit.concurrency.annotations.GuardedBy;
import com.google.code.tempusfugit.concurrency.annotations.ThreadSafe;

/**
 * @author aaugustyniak
 */
@ThreadSafe
public class MyApp {

    private int seq = 0;

    @GuardedBy(lock = GuardedBy.Type.CLASS)
    public synchronized int getNext() {
        return seq++;
    }

    public static void main(String[] args) {
        MyApp app = new MyApp();
        for (int i = 0; i < 10; i++) {
            System.out.println(app.getNext());
        }
    }
}
