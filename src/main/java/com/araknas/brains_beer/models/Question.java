package com.araknas.brains_beer.models;

import javax.persistence.*;

/**
 * Created by Giedrius on 2017.10.08.
 */
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer questionNumber;
    private String questionText;
    private String answerVariationsText;
    private String realAnswerText;
    private Integer timeToAnswer;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "round_id")
    private Round round;

    public Question() {
    }

    public Question(Integer questionNumber, String questionText, String answerVariationsText, String realAnswerText, Integer timeToAnswer, Round round, String imageUrl) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.answerVariationsText = answerVariationsText;
        this.realAnswerText = realAnswerText;
        this.timeToAnswer = timeToAnswer;
        this.round = round;
        this.imageUrl = imageUrl;
    }

    public Question(Integer questionNumber, String questionText, String answerVariationsText, String realAnswerText, Integer timeToAnswer, Round round) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.answerVariationsText = answerVariationsText;
        this.realAnswerText = realAnswerText;
        this.timeToAnswer = timeToAnswer;
        this.round = round;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getRealAnswerText() {
        return realAnswerText;
    }

    public void setRealAnswerText(String realAnswerText) {
        this.realAnswerText = realAnswerText;
    }

    public Integer getTimeToAnswer() {
        return timeToAnswer;
    }

    public void setTimeToAnswer(Integer timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
    }

    public String getAnswerVariationsText() {
        return answerVariationsText;
    }

    public void setAnswerVariationsText(String answerVariationsText) {
        this.answerVariationsText = answerVariationsText;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
