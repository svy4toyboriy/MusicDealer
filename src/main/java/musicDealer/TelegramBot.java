package musicDealer;//LINUX VERSION

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TelegramBot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TheMusicDealerBot";
    }

    @Override
    public String getBotToken() {
        // TODO
        return "6216419927:AAHiT0ICNp3EXEupBv2MzgdqZLAiSwVzOuY";
    }

    private static final int RESULTS_AMOUNT = 5;
    public static int lan;
    public static long CHAT_ID;
    public static String[][] phrases = {{"Отлично! Введите свой запрос.", "Выберите аудио:", "Скачиваю..",
            "Загружаю..", "А вот и оно!", "Временно недоступно. Выберите другую кнопку."}, {"Alright! Search for anything.", "Choose audio:", "Downloading..",
            "Uploading..", "Here it is!", "Temporary unavailable. Use another button"}};
    public static boolean isStart(Update update) {
        return update.getMessage().getText().compareTo("/start") == 0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            //Database.insertQuery(update);
            if (isStart(update)) {
                InlineKeyboardMarkup languageKeyboard = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> languages = new ArrayList<>();
                List<InlineKeyboardButton> row1 = new ArrayList<>();

                InlineKeyboardButton language1 = new InlineKeyboardButton();
                language1.setText("\uD83C\uDDF7\uD83C\uDDFA Русский");
                language1.setCallbackData("rus");
                row1.add(language1);
                InlineKeyboardButton language2 = new InlineKeyboardButton();
                language2.setText("\uD83C\uDDEC\uD83C\uDDE7 English");
                language2.setCallbackData("eng");
                row1.add(language2);

                languages.add(row1);
                languageKeyboard.setKeyboard(languages);

                CHAT_ID = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(CHAT_ID);
                sendMessage.setReplyMarkup(languageKeyboard);
                sendMessage.setText("Добро пожаловать! Выберите язык.\nWelcome! Choose language.");
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;
            }

            String text = update.getMessage().getText() + " audio";
            String Query = URLEncoder.encode(text, StandardCharsets.UTF_8);
            try {
                YouTube.search(Query, RESULTS_AMOUNT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows = new ArrayList<>();
            for (int i = 0; i < RESULTS_AMOUNT; i++) {
                String song = YouTube.audioTitle[i];
                Buttons.addButton(rows, song, i + 1);
            }

            keyboard.setKeyboard(rows);
            CHAT_ID = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(CHAT_ID);
            sendMessage.setReplyMarkup(keyboard);
            sendMessage.setText(phrases[lan][1]);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().length() > 1) {
                if (update.getCallbackQuery().getData().compareTo("rus") == 0) lan = 0; else lan = 1;
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(CHAT_ID);
                sendMessage.setText(phrases[lan][0]);
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(CHAT_ID);
            sendMessage.setText(phrases[lan][2]);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            int buttonNumber = Integer.parseInt(update.getCallbackQuery().getData()) - 1;
            String fileName = YouTube.audioTitle[buttonNumber].replaceAll("[^\\da-zA-Zа-яёА-ЯЁ]", "");
            String songUrl = YouTube.audioUrl[buttonNumber];
            //Database.insertButton(buttonNumber + 1, YouTube.audioTitle[buttonNumber], CHAT_ID);

            String audioPath = "/root/home/resources/Audio/downloads/" + fileName + ".m4a";
            File file = new File(audioPath);
            if (!file.exists()) {
                try {
                    Console.call(fileName, songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean isAvailable = true;
            if (!file.exists()) {
                sendMessage.setText(phrases[lan][5]);
                isAvailable = false;
            } else sendMessage.setText(phrases[lan][3]);

            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            if (!isAvailable) return;

            InputFile inputFile = new InputFile(file, audioPath);
            System.out.println(audioPath);
            SendAudio audio = new SendAudio();
            audio.setChatId(CHAT_ID);
            audio.setAudio(inputFile);
            audio.setTitle(YouTube.audioTitle[buttonNumber]);
            audio.setCaption(phrases[lan][4]);
            try {
                this.execute(audio);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws TelegramApiException, ClassNotFoundException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBot());
    }
}
