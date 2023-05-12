package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class DeliveryTest {
    @Test
    public void shouldSuccessfulFormSubmission() {
        open("http://localhost:9999");
        DataGenerator.UserInfo userData = DataGenerator.Registration.generateUser("Ru");
        //Заполнение и первоначальная отправка формы:
        $("[data-test-id=city] input").setValue(userData.getCity());
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String scheduledDate = DataGenerator.generateDate(4);   //Запланированная дата (текущая дата + 4 дня)
        $("[data-test-id=date] input").setValue(scheduledDate);
        $("[data-test-id=name] input").setValue(userData.getName());
        $("[data-test-id=phone] input").setValue(userData.getPhone());
        $("[data-test-id=agreement]").click();
        $(".button").shouldHave(Condition.text("Запланировать")).click();
        //Проверка на видимость, содержание текста и время загрузки:
        $("[data-test-id=success-notification]").shouldHave(Condition.visible)
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + scheduledDate),
                        Duration.ofSeconds(15));
        //Изменение ранее введенной даты и отправка формы:
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.DELETE);
        String rescheduledDate = DataGenerator.generateDate(14);   //Перенесенная дата (текущая дата + 14 дней)
        $("[data-test-id=date] input").setValue(rescheduledDate);
        $(".button").shouldHave(Condition.text("Запланировать")).click();
        //Взаимодействие с опцией перепланировки,
        //а также проверка на видимость, содержание текста и время загрузки:
        $("[data-test-id=replan-notification]").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Необходимо подтверждение" +
                                " У вас уже запланирована встреча на другую дату. Перепланировать?"),
                        Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] .button").shouldHave(Condition.text("Перепланировать")).click();
        //Итоговая проверка на видимость, содержание текста и время загрузки:
        $("[data-test-id=success-notification]").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Успешно! Встреча успешно запланирована на " + rescheduledDate),
                        Duration.ofSeconds(15));
    }
}