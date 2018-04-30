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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Giedrius on 2018-04-05.
 */
@Service
public class ImageService {
    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");
    public static final String DEFAULT_IMAGE_SAVE_LOCATION = "extra_data";
    public static final int BUFFER_READER_SIZE = 1024;
    public static final int THREAD_POOL_SIZE = 10;
    private ExecutorService serviceRunner = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    @Autowired
    ConnectionService connectionService;

    public List<Question> downloadQuestionsImages(List<Question> questions){
        try{
            String internetStatus = connectionService.checkInternetConnectionStatus();
            if(internetStatus.equals(ConnectionService.INTERNET_CONNECTION_STATUS_OK)){

                CountDownLatch latch = new CountDownLatch(questions.size());

                for(Question question : questions){
                    serviceRunner.execute(new TaskHandler(question, latch));
                }

                logger.info("Downloading "  + questions.size() + " images...");
                latch.await();
                logger.info("Images have been downloaded.");

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

    class TaskHandler implements Runnable {

        Question question;
        CountDownLatch latch;
        TaskHandler(Question question, CountDownLatch latch) {
            this.question = question;
            this.latch = latch;
        }

        public void run() {
            manageDownloadTask(this.question, latch);
        }
    }

    private void manageDownloadTask(Question question, CountDownLatch latch) {

        int questionNumb = -1;
        try{
            if(question != null){
                questionNumb = question.getQuestionNumber();
                logger.info("Downloading image for question nr. " + questionNumb);

                boolean isNeedToOverride = true;
                String localUrl = downloadImageForQuestion(question, isNeedToOverride);
                question.setImageUrl(localUrl);
            }
        }
        catch (Exception e){
            logger.error("Exception while downloading an image, e = " + e.getMessage());
            e.printStackTrace();
        }
        finally {

            logger.info("Downloading task is finished with question nr. " +  questionNumb);
            latch.countDown();
        }
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
