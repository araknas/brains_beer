package com.araknas.brains_beer.services;

import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by Giedrius on 2018-04-05.
 */
@Service
public class ImageService {
    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");
    public static final String DEFAULT_IMAGE_SAVE_LOCATION = "extra_data";
    public static final int BUFFER_READER_SIZE = 1024;

    @Autowired
    ConnectionService connectionService;

    public List<Question> downloadQuestionsImages(List<Question> questions){
        try{
            String internetStatus = connectionService.checkInternetConnectionStatus();
            if(internetStatus.equals(ConnectionService.INTERNET_CONNECTION_STATUS_OK)){

                boolean isAllImagesHandled = false;

                for(Question question : questions){
                    // TODO: need asynchronous download
                    boolean isNeedToOverride = true;
                    String localUrl = downloadImageForQuestion(question, isNeedToOverride);
                    question.setImageUrl(localUrl);
                }
            }
            else{
                logger.error("Cannot download images, no internet connection, msg = " + internetStatus);
            }
        }
        catch (Exception e){
            logger.error("Exception while downloading images, e = " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }

    public String downloadImageForQuestion(Question question, boolean isNeedToOverride) {

        String localUrl = "";
        try{
            Round round = question.getRound();
            String webUrl = question.getImageUrl();
            int questionNumber = question.getQuestionNumber();
            String roundTitle = round.getTitle();
            String imageDirectoryPath = DEFAULT_IMAGE_SAVE_LOCATION + File.separator + roundTitle;
            String imageName = "question_image_" + questionNumber + ".png";
            String imagePath = imageDirectoryPath + File.separator + imageName;

            File directory = new File(imageDirectoryPath);
            if (! directory.exists()){
                directory.mkdir();
            }

            File imageFile = new File(imagePath);
            if(imageFile.exists() && isNeedToOverride){
                Files.deleteIfExists(imageFile.toPath());
            }

            localUrl = downloadImage(webUrl, imagePath);

        }
        catch (Exception e){
            logger.error("Exception while downloading image, e = " + e.getMessage());
            e.printStackTrace();
        }
        return localUrl;
    }

    public String downloadImage(String webUrl, String imagePath) {

        String localUrl = "";

        try(InputStream in = new BufferedInputStream(new URL(webUrl).openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileOutputStream fos = new FileOutputStream(imagePath)){

            byte[] buf = new byte[BUFFER_READER_SIZE];

            int n = 0;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            byte[] response = out.toByteArray();

            fos.write(response);
            File imageFile = new File(imagePath);
            if(imageFile.exists()){
                localUrl = imagePath;
            }
        }
        catch (Exception e){
            logger.error("Exception while downloading image with url (" + webUrl + "), e = " + e.getMessage());
            e.printStackTrace();
        }
        return localUrl;
    }

}
