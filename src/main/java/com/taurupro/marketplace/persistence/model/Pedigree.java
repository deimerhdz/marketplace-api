package com.taurupro.marketplace.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pedigree {
    private String name;

    private String stud;

    private Pedigree sire;

    private Pedigree dam;
}
