package com.araknas.brains_beer.crud_tests;

import com.araknas.brains_beer.models.Team;
import com.araknas.brains_beer.repositories.TeamRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by Giedrius on 2017-10-09.
 */

@RunWith(SpringRunner.class)

@SpringBootTest
public class TeamCrudTests {

    @Autowired
    TeamRepository teamRepository;
    Team team1 = null;
    Team team2 = null;
    Team team3 = null;

    @Before
    public void setUp() {

        team1 = new Team();
        team1.setName("Pabaisiukai");
        team1.setMembersCount(3);

        team2 = new Team();
        team2.setName("Žagarėliai");
        team2.setMembersCount(3);

        team3 = new Team();
        team3.setName("Okvardukai");
        team3.setMembersCount(4);
    }

    @Test
    public void testAddingNewTeams(){

        teamRepository.save(team1);
        teamRepository.save(team2);
        teamRepository.save(team3);

        List<Team> teamList = teamRepository.findAll();
        Assert.assertTrue("Checking if teamList is not null", teamList != null);
        Assert.assertTrue("Checking if teamList size is greater than 0.", teamList.size() > 0);
    }
    @Test
    public void testGettingByTeamName(){

        teamRepository.save(team1);
        teamRepository.save(team2);
        teamRepository.save(team3);

        Team team = teamRepository.findByName("Okvardukai");
        Assert.assertTrue("Checking if team is not null", team != null);
        Assert.assertTrue("Checking if team has a correct name", team.getName().equals("Okvardukai"));
    }

}
