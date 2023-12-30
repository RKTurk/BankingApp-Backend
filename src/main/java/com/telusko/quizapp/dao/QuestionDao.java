package com.telusko.quizapp.dao;

import com.telusko.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDao extends JpaRepository<Question,Integer> {
    List<Question> findByCategory(String category);

    List<Question> findBydifficultyLevel(String difficultylevel);

        // For PostGreSQL
        //@Query(value = "SELECT * FROM question q WHERE q.category=:category ORDER BY RANDOM() LIMIT :numQ" ,nativeQuery = true)
    @Query(value ="SELECT * FROM question q WHERE q.category =:category ORDER BY RAND() LIMIT :numQ", nativeQuery = true)
    List<Question> findRandomQuestionsbyCategory(String category, Integer numQ);

    @Query(value ="select count(*) as counter, category  from questiondb.question q group by category", nativeQuery = true)
    List<Object[]> countByCategory();

}
