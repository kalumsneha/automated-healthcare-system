package com.titans.ahs.model.enums;

public enum Role {
    ADMIN, PATIENT;

    public boolean isAdmin(){
        return this.name().equalsIgnoreCase(Role.ADMIN.name());
    }

    public boolean isPatient(){
        return this.name().equalsIgnoreCase(Role.PATIENT.name());
    }
}
