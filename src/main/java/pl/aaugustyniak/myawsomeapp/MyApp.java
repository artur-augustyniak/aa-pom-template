package pl.aaugustyniak.myawsomeapp;

import com.google.code.tempusfugit.concurrency.annotations.GuardedBy;
import com.google.code.tempusfugit.concurrency.annotations.ThreadSafe;
import com.google.common.base.Optional;


/**
 * @author aaugustyniak
 */
@ThreadSafe
public class MyApp {


    @GuardedBy(lock = GuardedBy.Type.THIS)
    private int seq = 0;


    public synchronized int getNext() {
        return seq++;
    }


    public Optional anticipateAbsenceWithGuava() {
        return Optional.of(5);
    }

    public static void main(String[] args) {


        MyApp app = new MyApp();
        for (int i = 0; i < 10; i++) {
            System.out.println(app.getNext());
        }
    }
}
