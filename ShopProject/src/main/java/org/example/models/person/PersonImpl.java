package org.example.models.person;

import java.io.Serializable;

// I had a Person interface, but It seemed redundant, as everything that was in the interface was in the PersonImpl class and I dont see what else could inherit the
// Person interface, so I decided to remove it and have the PersonImpl class be an abstract class
public abstract class PersonImpl implements Serializable {
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
