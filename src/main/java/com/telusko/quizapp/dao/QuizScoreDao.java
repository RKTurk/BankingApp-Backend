
package com.telusko.quizapp.dao;
import com.telusko.quizapp.model.QuizScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizScoreDao extends JpaRepository<QuizScore, Long> {
    // You can add custom query methods if needed
}
