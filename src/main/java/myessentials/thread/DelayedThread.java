package myessentials.thread;

import java.lang.reflect.Method;

/**
 * A simple thread that is delayed by an amount of seconds which calls a given method.
 * Only use on methods that are thread-safe!
 */
public class DelayedThread extends Thread {

    private int delay;
    private Method method;
    private Object instance;
    private Object[] args;

    public DelayedThread(int delay, Method method, Object instance, Object... args) {
        this.delay = delay;
        this.method = method;
        this.instance = instance;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            sleep(delay * 1000);
            method.invoke(instance, args);
        } catch (Exception ex) {
            // TODO: Beware that the methods called here might not be synced
        }
    }
}
