package com.example.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Matches {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long who;
    private Long whom;

    public Matches() {
    }

    public Matches(Long who, Long whom) {
        this.who = who;
        this.whom = whom;
    }

    public Long getWho() {
        return who;
    }

    public void setWho(Long who) {
        this.who = who;
    }

    public Long getWhom() {
        return whom;
    }

    public void setWhom(Long whom) {
        this.whom = whom;
    }
}
