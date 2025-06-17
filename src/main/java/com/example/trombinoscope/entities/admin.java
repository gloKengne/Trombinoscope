package com.example.trombinoscope.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Data
@Setter
@Getter
@DiscriminatorValue("ADMIN")
public class admin extends user {
    public admin(){
        super();
        this.setRole(role.ADMIN);
    }

}
