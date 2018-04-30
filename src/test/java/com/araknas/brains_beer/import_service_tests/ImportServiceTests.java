package com.araknas.brains_beer.import_service_tests;

import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.services.ImportService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.PrintWriter;

/**
 * Created by Giedrius on 2018-04-30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportServiceTests {

    @Autowired
    ImportService importService;
    String roundFileName = "";
    String importFileData = "";
    String importDirectoryPath = "";
    String importFilePath = "";

    @Before
    public void setUp() throws Exception{
        importDirectoryPath = ImportService.DEFAULT_IMPORT_FILES_LOCATION + File.separator + "test_imports";
        roundFileName = "testRoundForImport.csv";
        importFileData = "blitz,TestRound\n" +
                "1,Pirmas klausimas?,Pirmas atsakymas,\"variantas1, variantas2\",-1,http://files.softicons.com/download/social-media-icons/simple-icons-by-dan-leech/png/256x256/imdb.png\n" +
                "2,Antras klausimas?,Antras atsakymas,\"\",-1,http://files.softicons.com/download/social-media-icons/simple-icons-by-dan-leech/png/256x256/imdb.png\n" +
                "3,Tre?ias klausimas?,Tre?ias atsakymas,\"variantas1, variantas2\",-1,http://files.softicons.com/download/social-media-icons/simple-icons-by-dan-leech/png/256x256/imdb.png\n" +
                "4,Ketvirtas klausimas?,Ketvirtas atsakymas,\"\",-1,http://files.softicons.com/download/social-media-icons/simple-icons-by-dan-leech/png/256x256/imdb.png";

        File directory = new File(ImportService.DEFAULT_IMPORT_FILES_LOCATION);
        if (! directory.exists()){
            directory.mkdir();
        }

        directory = new File(importDirectoryPath);
        if (! directory.exists()){
            directory.mkdir();
        }
        importFilePath = importDirectoryPath + File.separator + roundFileName;

        File importFile = new File(importFilePath);
        if (! importFile.exists()){

            PrintWriter writer = new PrintWriter(importFilePath, "UTF-8");
            writer.print(importFileData);
            writer.close();
        }
    }

    @Test
    public void testImportRound() throws Exception{
        Round round = importService.importRoundFromCsv(importFilePath);
        Assert.assertTrue("Round cannot be equal to null", round != null);

        //TODO: test if round and question(s) are actually saved
    }
}
