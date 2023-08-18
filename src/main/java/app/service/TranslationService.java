package app.service;

import app.dao.Word;
import app.dao.repository.WordRep;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class TranslationService {
    @Autowired
    private WordRep wordRep;
    @Autowired
    private ApiKeyService apiKeyService;
    public String getRusTranslation(String engWord){
        long time = System.currentTimeMillis();
        try {
            apiKeyService.createApiKey("smart-pride-312018") ;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Translate translate = TranslateOptions.getDefaultInstance().getService();
        List<Word> rusWord = wordRep.findByUsersId(759230168);
        List<String> word = new ArrayList<>();
        for(int x = 0;x <30;x ++){
            for(Word w: rusWord){
                word.add(w.getEngTranslation());
            }

        }
        //long time = System.currentTimeMillis();
        List<Translation> translations = translate.translate(word, Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage("ru"));
        translations.stream().forEach(System.out::println);
        System.out.println("Time " + (System.currentTimeMillis()-time));
        return "finish";
    }
}
