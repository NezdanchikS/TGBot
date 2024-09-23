package com.Nezdanchik.spbpu;

import com.Nezdanchik.spbpu.model.FilmModel;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ComponentScan("com.Nezdanchik.spbpu")
public class Main {
    public static void main(String[] args) {
        // Загрузка контекста Spring
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        // Получение экземпляра вашего бота
//        TelegramMovieBot bot = context.getBean(TelegramMovieBot.class);


        MongoClient mongoClient = context.getBean(MongoClient.class);
        var collection = mongoClient.getDatabase("admin").getCollection("new_collection");
//        collection.
        collection.updateOne(Filters.eq("_id", 2L), updateDBOject(createFilmModel()));
//        collection.insertOne(createDBObject(createFilmModel()));
        System.out.println(collection);
        // Регистрация бота вручную
//        try {
//            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//            botsApi.registerBot(bot);
//            System.out.println("Bot successfully started!");
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
    }

    private static Document createDBObject(FilmModel filmModel) {
        return new Document(Map.of(
                "_id", filmModel.getId(),
                "description", filmModel.getDescription(),
                "name_ru", filmModel.getNameRu(),
                "rating_kinopoisk", filmModel.getRatingKinopoisk(),
                "year", filmModel.getYear()
        ));
    }

    private static Bson updateDBOject(FilmModel filmModel) {
        return Updates.combine(
                Updates.set("description", filmModel.getDescription()),
                Updates.set("name_ru", filmModel.getNameRu()),
                Updates.set("rating_kinopoisk", filmModel.getRatingKinopoisk()),
                Updates.set("year", filmModel.getYear())
        );
    }

    private static FilmModel createFilmModel() {
        return FilmModel.builder()
                .id(5L)
                .year(2003)
                .nameRu("Название")
                .description("Описание")
                .ratingKinopoisk(1112.)
                .build();
    }
}
