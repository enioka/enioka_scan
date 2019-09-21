package com.enioka.scanner.bt;


/**
 * Returned by a {@link ICommand}. This gives all the data needed to actually run and wait for the result.
 *
 * @param <T>
 */
public class CommandCallbackHolder<T> {
    private final CommandCallback<T> callback;
    private final boolean permanent;
    private final Class<? extends T> cbType;
    private final int timeOutMs;

    public CommandCallbackHolder(Class<? extends T> type, CommandCallback<T> callback, boolean permanent, int timeOutMs) {
        this.callback = callback;
        this.permanent = permanent;
        this.cbType = type;
        this.timeOutMs = timeOutMs;
    }

    CommandCallback<T> getCallback() {
        return this.callback;
    }

    Class<? extends T> getCommandReturnType() {
        return this.cbType;
    }

    boolean isPermanent() {
        return this.permanent;
    }

    int getTimeOutMs() {
        return this.timeOutMs;
    }
}
