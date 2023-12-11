package com.example.finalproject.Classes;

public class MyPair<T, U> {
    private final T first;
    private final U second;

    public MyPair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
