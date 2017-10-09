package com.araknas.brains_beer.models;


import javax.persistence.*;

/**
 * Created by Giedrius on 2017.10.08.
 */
@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer membersCount;

    public Team() {
    }

    public Team(String name, Integer membersCount) {
        this.name = name;
        this.membersCount = membersCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Integer membersCount) {
        this.membersCount = membersCount;
    }

    @Override
    public String toString() {
        return "team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + membersCount +
                '}';
    }
}
