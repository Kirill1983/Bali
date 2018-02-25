package org.kkonoplev.bali.classifyreport;

public class Dog extends Animal {
    private boolean playsCatch;

    public Dog(String name, boolean playsCatch) {
        super(name);
        this.playsCatch = playsCatch;
    }

    @Override
    public String toString() {
        return "Dog [name=" + name + ", playsCatch=" + playsCatch + "]";
    }
}