package lesson7;

//1. Реализовать корректный вывод информации о текущей погоде. Создать класс WeatherResponse и десериализовать ответ сервера. Выводить пользователю только текстовое описание погоды и температуру в градусах Цельсия.
//2. Реализовать корректный выход из программы
//3. Реализовать вывод погоды на следующие 5 дней в формате
//| В городе CITY на дату DATE ожидается WEATHER_TEXT, температура - TEMPERATURE |
//
//где CITY, DATE, WEATHER_TEXT и TEMPERATURE - уникальные значения для каждого дня.

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserInterfaceView userInterfaceView = new UserInterfaceView();
        userInterfaceView.runInterface();
        DataBaseRepository dataBaseRepository = new DataBaseRepository();
        System.out.println(dataBaseRepository.getSavedToDB());
    }
}
