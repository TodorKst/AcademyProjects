package org.example.models.person;

import java.io.Serializable;

// There is no need for a person interface,
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
