package musicDealer;

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
import java.util.ArrayList;
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
    public static long CHAT_ID;

    public static boolean isStart(Update update) {
        return update.getMessage().getText().compareTo("/start") == 0;
    }
    
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (isStart(update)) {
                CHAT_ID = update.getMessage().getChatId();
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(CHAT_ID);
                sendMessage.setText("I'm ready. Go ahead!");
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
            sendMessage.setText("Choose a song:");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else if (update.hasCallbackQuery()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(CHAT_ID);
            sendMessage.setText("Downloading the song... It might take a while");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            
            int buttonNumber = Integer.parseInt(update.getCallbackQuery().getData()) - 1;
            String fileName = YouTube.audioTitle[buttonNumber].replaceAll("[^\\da-zA-Zа-яёА-ЯЁ]", "");
            String songUrl = YouTube.audioUrl[buttonNumber];

            String audioPath = "D:\\Audio\\downloads\\" + fileName + ".m4a";
            File file = new File(audioPath);
            if (!file.exists()) {
                try {
                    Console.call(fileName, songUrl);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            sendMessage.setText("Uploading...");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

            InputFile inputFile = new InputFile(file, audioPath);
            System.out.println(audioPath);
            SendAudio audio = new SendAudio();
            audio.setChatId(CHAT_ID);
            audio.setAudio(inputFile);
            audio.setTitle(YouTube.audioTitle[buttonNumber]);
            audio.setCaption("Here it is!");
            try {
                this.execute(audio);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TelegramBot());
    }
}
