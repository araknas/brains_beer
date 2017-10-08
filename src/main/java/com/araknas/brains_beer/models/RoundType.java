package com.araknas.brains_beer.models;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Giedrius on 2017.10.08.
 */

@Entity
@Table(name = "round_type")
public class RoundType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @OneToMany(mappedBy = "roundType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Round> rounds;

    public RoundType() {
    }

    public RoundType(String title, Set<Round> rounds) {
        this.title = title;
        this.rounds = rounds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Round> getRounds() {
        return rounds;
    }

    public void setRounds(Set<Round> rounds) {
        this.rounds = rounds;
    }
}
