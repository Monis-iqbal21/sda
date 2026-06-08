package com.fixmate.patterns.nullobject;

import com.fixmate.model.User;

public class NullUser extends User {

    public NullUser() {
        super(0L, "Unknown", "unknown@fixmate.com", "", User.Role.CLIENT);
    }

    public boolean isNull() {
        return true;
    }
}
