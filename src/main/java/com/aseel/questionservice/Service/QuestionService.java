package com.aseel.questionservice.Service;

import com.aseel.questionservice.Model.Question;
import com.aseel.questionservice.Model.QuestionWrapper;
import com.aseel.questionservice.Model.Response;
import com.aseel.questionservice.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll() , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category) , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        try {
            return new ResponseEntity<>("Question added successfully" , HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Question added successfully" , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz
            (String category, int numQuestions) {
        List<Integer> questions =
                questionDao.findRandomQuestionsByCategory(category , numQuestions);
        return  new ResponseEntity<>(questions , HttpStatus.OK);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId
            (List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Integer id : questionIds) {
            questions.add(questionDao.findById(id).get());
        }

        for (Question question : questions){
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers , HttpStatus.OK);
    }


    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int score = 0;
        for ( Response response : responses) {
            Optional<Question> question = questionDao.findById
                    (response.getId());
            if (response.getResponse().equals(question.get().getRightAnswer()))
                score++;

        }
        return  new ResponseEntity<>(score , HttpStatus.OK);
    }
}
