package org.util;

public final class Ref<T> {
    public T value;

    public Ref(T value) {
        this.value = value;
    }

    public Ref() {
        this.value = null;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}

