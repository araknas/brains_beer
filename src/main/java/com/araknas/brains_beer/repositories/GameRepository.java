package com.araknas.brains_beer.repositories;

import com.araknas.brains_beer.models.Game;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */
public interface GameRepository extends CrudRepository<Game, Integer> {

    List<Game> findAll();
    Game findByGameTitle(String gameTitle);
}
