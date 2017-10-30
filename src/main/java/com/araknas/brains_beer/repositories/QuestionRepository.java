package com.araknas.brains_beer.repositories;

import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.RoundType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */
public interface QuestionRepository extends CrudRepository<Question, Integer> {

    List<Question> findAll();
    List<Question> findByRound(Round round);
}
