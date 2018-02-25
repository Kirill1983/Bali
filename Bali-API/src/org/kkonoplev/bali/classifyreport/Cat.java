package org.kkonoplev.bali.classifyreport;

public class Cat extends Animal {
    private boolean chasesLaser;

    public Cat(String name, boolean chasesLaser) {
        super(name);
        this.chasesLaser = chasesLaser;
    }

    @Override
    public String toString() {
        return "Cat [name=" + name + ", chasesLaser=" + chasesLaser + "]";
    }
}