package com.example.project.domain;

import javax.persistence.*;

@Entity
public class Redflag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

/*    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userid", referencedColumnName = "id")*/
    private Long userid;
    private String text;

    public Redflag() {
    }

    public Redflag(Long userid, String text) {
        this.userid = userid;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
