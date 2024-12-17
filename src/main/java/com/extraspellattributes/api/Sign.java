package com.extraspellattributes.api;

public enum Sign {
    //Code Credit to Pufferfish
    POSITIVE,
    NEGATIVE;

    public <T> Signed<T> wrap(T value) {
        return new Signed<>(this, value);
    }
}