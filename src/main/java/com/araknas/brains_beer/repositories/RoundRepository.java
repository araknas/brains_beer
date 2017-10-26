package com.araknas.brains_beer.repositories;

import com.araknas.brains_beer.models.Game;
import com.araknas.brains_beer.models.Round;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */
public interface RoundRepository extends CrudRepository<Round, Integer> {

    List<Round> findAll();
    Round findByTitle(String title);
}
