package com.araknas.brains_beer.repositories;

import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.RoundType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */
public interface RoundTypeRepository extends CrudRepository<RoundType, Integer> {

    List<RoundType> findAll();
    RoundType findByTitle(String title);
}
