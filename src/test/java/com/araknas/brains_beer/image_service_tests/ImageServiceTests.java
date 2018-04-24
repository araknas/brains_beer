package com.araknas.brains_beer.image_service_tests;

import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.services.ImageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;

/**
 * Created by Giedrius on 2018-04-24.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImageServiceTests {

    @Autowired
    ImageService imageService;
    String testImageWebUrl;
    Question question;

    @Before
    public void setUp() {
        testImageWebUrl = "https://upload.wikimedia.org/wikipedia/commons/5/57/CFDG_Forest.png";
        question = new Question();
        Round round = new Round();
        round.setTitle("testRound");

        question.setRound(round);
        question.setImageUrl(testImageWebUrl);
        question.setQuestionNumber(1);
    }

    @Test
    public void testDownloadImageForQuestion() throws Exception{

        boolean isNeedToOverride = true;
        String localUrl = imageService.downloadImageForQuestion(question, isNeedToOverride);
        File file = new File(localUrl);

        Assert.assertTrue("Test question image file wasn't downloaded", file.exists());
    }

    @Test
    public void testDownloadImage() throws Exception{
        String imageDirectoryPath = ImageService.DEFAULT_IMAGE_SAVE_LOCATION + File.separator + "test_images";
        String imageName = "test_image.png";
        String testImagePath = imageDirectoryPath + File.separator + imageName;

        File file = new File(testImagePath);
        Files.deleteIfExists(file.toPath());
        Assert.assertFalse("Test image file wasn't deleted", file.exists());

        File directory = new File(imageDirectoryPath);
        if (! directory.exists()){
            directory.mkdir();
        }

        imageService.downloadImage(testImageWebUrl, testImagePath);
        Assert.assertTrue("Test image file wasn't downloaded", file.exists());
    }
}
