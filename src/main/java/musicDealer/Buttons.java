package musicDealer;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    public static void addButton(List<List<InlineKeyboardButton>> l, String name, int i) {
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(i + ". " + name);
        inlineKeyboardButton1.setCallbackData(i + "");
        rowInline1.add(inlineKeyboardButton1);
        l.add(rowInline1);
    }
}
