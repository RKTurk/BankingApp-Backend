package com.telusko.quizapp.service;


import com.telusko.quizapp.dao.QuestionDao;
import com.telusko.quizapp.dao.QuizDao;
import com.telusko.quizapp.dao.QuizScoreDao;
import com.telusko.quizapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    private QuizScoreDao quizScoreDao;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Question> questions = questionDao.findRandomQuestionsbyCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return  new ResponseEntity<>("Success", HttpStatus.CREATED);



    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
      Optional<Quiz> quiz = quizDao.findById(id);
      List<Question> questionsFromDB = quiz.get().getQuestions();
      List<QuestionWrapper> questionsForUser = new ArrayList<>();
      for(Question q: questionsFromDB)
      {
          QuestionWrapper qw = new QuestionWrapper(q.getId(),q.getQuestion(),q.getOption1(),q.getOption2(),q.getOption3(),q.getOption4());
          questionsForUser.add(qw);
      }


      return new ResponseEntity<>(questionsForUser, HttpStatus.OK);

    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses, String username) {
        Quiz quiz = quizDao.findById(id).get();
        List<Question> questions = quiz.getQuestions();
        int right = 0;
        int i=0;
        for (Response response : responses) {
            for (Question question : questions) {
                if (response.getId().equals(question.getId()) && response.getResponse().equals(question.getCorrectAnswer())) {
                    right++;
                    break; // Move to the next response once a match is found
                }
            }
            i++; // Calculate the total number of questions in the quiz
        }
        // Store in the database
        QuizScore quizScore = new QuizScore();
        quizScore.setScore(right);
        quizScore.setTotal_score(i);
        quizScore.setName(username);
        quizScore.setAttemptedOn(new Date());
        quizScoreDao.save(quizScore);
        return new ResponseEntity<>(right,HttpStatus.OK);
    }

    public ResponseEntity<List<Quiz>> getAllQuestions() {
        try {
            return new ResponseEntity<>(quizDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuizScore>> getAllQuizScore() {
        try {
            return new ResponseEntity<>(quizScoreDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }
}
