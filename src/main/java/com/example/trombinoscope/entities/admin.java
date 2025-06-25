package com.example.trombinoscope.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@DiscriminatorValue("ADMIN")
public class admin extends User {
    public admin(){
        super();
        this.setRole(User.Role.ADMIN);
    }

}
