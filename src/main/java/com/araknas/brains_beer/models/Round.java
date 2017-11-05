package com.araknas.brains_beer.models;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by Giedrius on 2017.10.08.
 */
@Entity
@Table(name = "round")
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "round_type_id")
    private RoundType roundType;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> questions;

    public Round() {
    }

    public Round(String title, RoundType roundType, List<Question> questions) {
        this.title = title;
        this.roundType = roundType;
        this.questions = questions;
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


    public RoundType getRoundType() {
        return roundType;
    }

    public void setRoundType(RoundType roundType) {
        this.roundType = roundType;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
