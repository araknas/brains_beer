package com.araknas.brains_beer.services;

import com.araknas.brains_beer.models.Question;
import com.araknas.brains_beer.models.Round;
import com.araknas.brains_beer.models.RoundType;
import com.araknas.brains_beer.repositories.QuestionRepository;
import com.araknas.brains_beer.repositories.RoundRepository;
import com.araknas.brains_beer.repositories.RoundTypeRepository;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Giedrius on 2018-04-30.
 */
@Service
public class ImportService {
    public static Logger logger = LoggerFactory.getLogger("brainsBeerLogger");
    public static final int DEFAULT_IMPORT_FILE_HEADER_COLUMNS_COUNT = 2;
    public static final int DEFAULT_IMPORT_FILE_DATA_COLUMNS_COUNT = 6;
    public static final String DEFAULT_IMPORT_FILES_LOCATION = "data_for_import";

    @Autowired
    RoundRepository roundRepository;
    @Autowired
    RoundTypeRepository roundTypeRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ImageService imageService;

    public void importRounds(){

        try{
            File folder = new File(DEFAULT_IMPORT_FILES_LOCATION);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String pathToImportFile = ImportService.DEFAULT_IMPORT_FILES_LOCATION + File.separator +
                            listOfFiles[i].getName();
                    importRoundFromCsv(pathToImportFile);
                }
            }
        }
        catch (Exception e){
            logger.error("Exception while importing rounds");
            e.printStackTrace();
        }
    }

    public Round importRoundFromCsv(String pathToRoundFile) {
        Round round = null;
        //TODO: fix encoding

        try(CSVReader csvReader = new CSVReader(new FileReader(pathToRoundFile), ',')){

            String[] record = null;
            record = csvReader.readNext();
            if(record != null && record.length == DEFAULT_IMPORT_FILE_HEADER_COLUMNS_COUNT){
                round = new Round();

                RoundType roundType = roundTypeRepository.findByTitle(record[0]);
                round.setRoundType(roundType);
                round.setTitle(record[1]);
                roundRepository.save(round);

                List<Question> questionList = new ArrayList();

                while((record = csvReader.readNext()) != null){
                    if(record.length == DEFAULT_IMPORT_FILE_DATA_COLUMNS_COUNT){
                        Question question = new Question();
                        question.setQuestionNumber(Integer.valueOf(record[0]));
                        question.setQuestionText(record[1]);
                        question.setRealAnswerText(record[2]);
                        question.setAnswerVariationsText(record[3]);
                        question.setTimeToAnswer(Integer.valueOf(record[4]));
                        question.setImageUrl(record[5]);
                        question.setRound(round);
                        questionList.add(question);
                    }
                }
                imageService.downloadQuestionsImages(questionList);
                questionRepository.save(questionList);
                round.setQuestions(questionList);
            }
        }
        catch (Exception e){
            logger.error("Exception while importing round from " + pathToRoundFile);
            e.printStackTrace();
        }

        return round;
    }
}
