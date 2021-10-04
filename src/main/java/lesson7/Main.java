package lesson7;

//1. Добавить поддержку SQLite в проект.
//2. Создать класс-репозиторий, отвечающий за взаимодействие с базой данных.
//3. Организовать запись данных в базу при каждом успешном API запросе. Формат - String city, String localDate, String weatherText, Double temperature.
//4. Организовать чтение из базы всех данных по пункту меню (требует переработки меню)
//5. Учесть, что соединение всегда нужно закрывать

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserInterfaceView userInterfaceView = new UserInterfaceView();
        userInterfaceView.runInterface();
        DataBaseRepository dataBaseRepository = new DataBaseRepository();
        System.out.println(dataBaseRepository.getSavedToDB());
    }
}
