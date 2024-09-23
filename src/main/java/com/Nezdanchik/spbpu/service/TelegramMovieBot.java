package com.Nezdanchik.spbpu.service;

import com.Nezdanchik.spbpu.config.BotConfig;
import com.Nezdanchik.spbpu.model.FilmModel;
import com.Nezdanchik.spbpu.model.Film;
import com.Nezdanchik.spbpu.model.WatchlistItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
@AllArgsConstructor
public class TelegramMovieBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final MovieService movieService;
    private final UserService userService;
    private final WatchlistService watchlistService;
    private final PreferenceService preferenceService;

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        FilmModel filmModel = new FilmModel();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Получаем или создаем пользователя
            var user = userService.getOrCreateUser(chatId);

            if (messageText.startsWith("/")) {
                handleCommand(messageText, chatId, user, update);
            } else {
                // Предположим, что пользователь вводит filmId
                try {
                    String movieInfo = movieService.getMovie(messageText, filmModel);
                    sendMessage(chatId, movieInfo);
                } catch (IOException | ParseException e) {
                    sendMessage(chatId, "Не удалось получить информацию о фильме.");
                }
            }
        }
    }

    private void handleCommand(String messageText, long chatId, com.Nezdanchik.spbpu.model.User user, Update update) {
        String[] parts = messageText.split(" ", 2);
        String command = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        switch (command) {
            case "/start":
                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;

            case "/show_preferences":
                String prefs = preferenceService.showPreferences(user);
                sendMessage(chatId, prefs);
                break;

            case "/add_preferences":
                handleAddPreferences(chatId, args, user);
                break;

            case "/delete_preferences":
                handleDeletePreferences(chatId, args, user);
                break;

            case "/find_film":
                handleFindFilm(chatId, args, user);
                break;

            case "/random_film":
                handleRandomFilm(chatId, user);
                break;

            case "/add_to_watchlist":
                handleAddToWatchlist(chatId, args, user);
                break;

            case "/show_watchlist":
                handleShowWatchlist(chatId, user);
                break;

            case "/mark_watched":
                handleMarkWatched(chatId, args, user);
                break;

            case "/remove_from_watchlist":
                handleRemoveFromWatchlist(chatId, args, user);
                break;

            default:
                sendMessage(chatId, "Неизвестная команда.");
        }
    }

    private void handleAddPreferences(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        // Ожидаем формат: жанры=Комедия,Драма; актеры=Имя1,Имя2; год=2020
        Map<String, String> params = parseArgs(args);
        Set<String> genres = params.containsKey("жанры") ? Set.of(params.get("жанры").split(",")) : null;
        Set<String> actors = params.containsKey("актеры") ? Set.of(params.get("актеры").split(",")) : null;
        Integer year = params.containsKey("год") ? Integer.parseInt(params.get("год")) : null;

        preferenceService.addPreferences(user, genres, actors, year);
        sendMessage(chatId, "Предпочтения добавлены.");
    }

    private void handleDeletePreferences(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        // Ожидаем формат: жанры=Комедия,Драма; актеры=Имя1,Имя2; год=2020
        Map<String, String> params = parseArgs(args);
        Set<String> genres = params.containsKey("жанры") ? Set.of(params.get("жанры").split(",")) : null;
        Set<String> actors = params.containsKey("актеры") ? Set.of(params.get("актеры").split(",")) : null;
        Integer year = params.containsKey("год") ? Integer.parseInt(params.get("год")) : null;

        preferenceService.deletePreferences(user, genres, actors, year);
        sendMessage(chatId, "Предпочтения удалены.");
    }

    private void handleFindFilm(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        // Ожидаем формат: ключ1=значение1; ключ2=значение2 и т.д.
        Map<String, String> params = parseArgs(args);
        boolean skipGenre = Boolean.parseBoolean(params.getOrDefault("skip_genre", "false"));
        boolean skipActors = Boolean.parseBoolean(params.getOrDefault("skip_actors", "false"));
        boolean skipYear = Boolean.parseBoolean(params.getOrDefault("skip_year", "false"));

        List<Film> films = movieService.findFilms(user, skipGenre, skipActors, skipYear);
        if (films.isEmpty()) {
            sendMessage(chatId, "Не найдено фильмов по заданным критериям.");
        } else {
            StringBuilder response = new StringBuilder("Найденные фильмы:\n");
            for (Film film : films) {
                response.append("ID: ").append(film.getId())
                        .append(", Название: ").append(film.getTitle())
                        .append(", Рейтинг: ").append(film.getRating())
                        .append("\n");
            }
            sendMessage(chatId, response.toString());
        }
    }

    private void handleRandomFilm(long chatId, com.Nezdanchik.spbpu.model.User user) {
        Film film = movieService.getRandomFilm(user);
        if (film == null) {
            sendMessage(chatId, "Не удалось найти случайный фильм.");
        } else {
            sendMessage(chatId, "Случайный фильм:\nID: " + film.getId() + ", Название: " + film.getTitle() + ", Рейтинг: " + film.getRating());
        }
    }

    private void handleAddToWatchlist(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        try {
            Long filmId = Long.parseLong(args.trim());
            watchlistService.addToWatchlist(user, filmId);
            sendMessage(chatId, "Фильм добавлен в список для просмотра.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID фильма.");
        }
    }

    private void handleShowWatchlist(long chatId, com.Nezdanchik.spbpu.model.User user) {
        List<WatchlistItem> watchlist = watchlistService.getWatchlist(user);
        if (watchlist.isEmpty()) {
            sendMessage(chatId, "Ваш список для просмотра пуст.");
        } else {
            StringBuilder response = new StringBuilder("Ваш список для просмотра:\n");
            for (WatchlistItem item : watchlist) {
                response.append("ID: ").append(item.getFilmId())
                        .append(", Просмотрен: ").append(item.getWatched() ? "Да" : "Нет")
                        .append("\n");
            }
            sendMessage(chatId, response.toString());
        }
    }

    private void handleMarkWatched(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        try {
            Long filmId = Long.parseLong(args.trim());
            watchlistService.markWatched(user, filmId);
            sendMessage(chatId, "Фильм отмечен как просмотренный.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID фильма.");
        }
    }

    private void handleRemoveFromWatchlist(long chatId, String args, com.Nezdanchik.spbpu.model.User user) {
        try {
            Long filmId = Long.parseLong(args.trim());
            watchlistService.removeFromWatchlist(user, filmId);
            sendMessage(chatId, "Фильм удален из списка для просмотра.");
        } catch (NumberFormatException e) {
            sendMessage(chatId, "Неверный формат ID фильма.");
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Привет, " + name + "! Я помогу тебе находить фильмы.\n" +
                "Доступные команды:\n" +
                "/show_preferences - показать текущие предпочтения\n" +
                "/add_preferences - добавить предпочтения\n" +
                "/delete_preferences - удалить предпочтения\n" +
                "/find_film [query] - найти фильм по критериям\n" +
                "/random_film - предложить случайный фильм\n" +
                "/add_to_watchlist [film_id] - добавить фильм в список для просмотра\n" +
                "/show_watchlist - показать список для просмотра\n" +
                "/mark_watched [film_id] - отметить фильм как просмотренный\n" +
                "/remove_from_watchlist [film_id] - удалить фильм из списка для просмотра";
        sendMessage(chatId, answer);
    }

    private Map<String, String> parseArgs(String args) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = args.split(";");
        for (String pair : pairs) {
            String[] keyValue = pair.trim().split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0].trim().toLowerCase(), keyValue[1].trim());
            }
        }
        return params;
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
