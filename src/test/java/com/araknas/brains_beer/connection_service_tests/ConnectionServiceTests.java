package com.araknas.brains_beer.connection_service_tests;

import com.araknas.brains_beer.services.ConnectionService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Giedrius on 2018-04-24.
 */
@RunWith(SpringRunner.class)

@SpringBootTest
public class ConnectionServiceTests {

    @Autowired
    ConnectionService connectionService;

    @Before
    public void setUp(){

    }

    @Test
    public void testInternetConnectionChecker(){
        String connectionStatus = connectionService.checkInternetConnectionStatus();
        Assert.assertNotEquals("Connection status message cannot be null", null, connectionStatus);
        Assert.assertNotEquals("Connection status message cannot be empty", "", connectionStatus);
    }
}
