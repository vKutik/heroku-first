package com.example.price;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class CalculateBot extends TelegramLongPollingBot {
    public static final Double TAX_PROCENT = 0.05;
    public static final Double TAX_SOCIAL = 1430.0;
    public static final Double MATE_PROCENT = 0.17;

    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Double salary = 0.0;
        Double myMoney = 0.0;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                String text = message.getText();
                SendMessage sm = new SendMessage();
                salary = Double.parseDouble(text);
                if (salary == (double) salary) {
                    myMoney = salary;
                    myMoney -= salary * TAX_PROCENT;
                    myMoney -= TAX_SOCIAL;
                    sm.setText("Налог 5% : " + salary * TAX_PROCENT + "\n" +
                            "ЕСВ : " + TAX_SOCIAL + "\n" +
                            "Деньги после нагога : " + myMoney + "\n" +
                            "Mate : " + myMoney * MATE_PROCENT + "\n" +
                            "Чистый доход : " + (myMoney - (myMoney * MATE_PROCENT)));
                    sm.setChatId(String.valueOf(message.getChatId()));
                }
                try {
                    execute(sm);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
