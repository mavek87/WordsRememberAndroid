package com.matteoveroni.wordsremember.pojo;

public class Vocable extends Word {

    private final long id;

    public Vocable(long id, String name) {
        super(name);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Vocable vocable = (Vocable) o;

        return id == vocable.id;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
