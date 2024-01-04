package com.telusko.quizapp.model;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "quizscore")
public class QuizScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score")
    private Integer score;

    @Column(name = "total_score")
    private Integer total_score;

    @Column(name = "name")
    private String name;

    @Column(name = "attempted_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date attemptedOn;



}
