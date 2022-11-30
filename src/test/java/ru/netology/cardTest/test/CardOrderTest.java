package ru.netology.cardTest.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.cardTest.data.DataGenerator;


import java.time.Duration;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class CardOrderTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldCardOrderNewData() {
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $("[data-test-id='city'] input").setValue(DataGenerator.generateCity("ru"));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(DataGenerator.generateDate(4));
        $x("//*[@name=\"name\"]").setValue(DataGenerator.generateName("ru"));
        $x("//*[@name=\"phone\"]").setValue(DataGenerator.generatePhone("ru"));

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Запланировать")).click();
        $("[data-test-id=\"success-notification\"]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(DataGenerator.generateDate(7));
        $(withText("Запланировать")).click();

        $("[data-test-id=\"replan-notification\"]")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату."));
        $(withText("Перепланировать")).click();
        $("[data-test-id=\"success-notification\"]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }
    @Test
    public void shouldNameLetterE() {
        $("[data-test-id= 'city'] input").setValue(DataGenerator.generateCity("ru"));
        $("[data-test-id= 'date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(4));
        $("[data-test-id= 'name'] input").setValue(DataGenerator.generateNameLetterE("ru"));
        $("[data-test-id= 'phone'] input").setValue(DataGenerator.generatePhone("ru"));
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $(".notification_visible")
                .shouldBe(exist, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__title")
                .shouldHave(text("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + DataGenerator.generateDate(4)), Duration.ofSeconds(15));
    }

    @Test
    public void shouldApplicationFormShortPhone() {
        $("[data-test-id= 'city'] input").setValue(DataGenerator.generateCity("ru"));
        $("[data-test-id= 'date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id= 'date'] input").setValue(DataGenerator.generateDate(4));
        $("[data-test-id= 'name'] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id= 'phone'] input").setValue(DataGenerator.generateShortPhone("ru"));
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Запланировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
}





