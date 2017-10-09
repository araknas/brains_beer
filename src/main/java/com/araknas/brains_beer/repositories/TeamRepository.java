package com.araknas.brains_beer.repositories;

import com.araknas.brains_beer.models.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */
public interface TeamRepository extends CrudRepository<Team, Integer> {

    List<Team> findAll();
    Team findByName(String name);
}
