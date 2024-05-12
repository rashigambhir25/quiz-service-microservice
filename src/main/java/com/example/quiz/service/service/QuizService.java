package com.example.quiz.service.service;

import com.example.quiz.service.Feign.QuizInterface;
import com.example.quiz.service.model.QuestionWrapper;
import com.example.quiz.service.model.Quiz;
import com.example.quiz.service.model.UserResponse;
import com.example.quiz.service.repo.QuizRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizRepo quizRepo;
    @Autowired
    QuizInterface quizInterface;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionsIds(questions);
        quizRepo.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestion(Integer id) {
        Optional<Quiz> quiz = quizRepo.findById(id);
        List<Integer> questionIds = quiz.get().getQuestionsIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromIds(questionIds);
        return questions;
    }

    public ResponseEntity<Integer> getResult(Integer id, List<UserResponse> userResponses) {
        ResponseEntity<Integer> score = quizInterface.getScore(userResponses);
        return score;
    }
}
