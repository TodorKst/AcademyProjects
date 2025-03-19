package models.person;

import models.contracts.Person;

public abstract class PersonImpl implements Person {
    private final String id;
    private String name;

    public PersonImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PersonImpl() {
        this.id = null;
        this.name = null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
