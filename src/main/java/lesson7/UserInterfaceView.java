package lesson7;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterfaceView {
    private Controller controller = new Controller();
    public void runInterface() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите название города:");
            String city = scanner.nextLine();
            System.out.println("Введите <1> - для прогноза на 1 день, <5> - для прогноза на 5 дней, <3> - для чтения данных из базы. Для выхода введите 0.");
            String command = scanner.nextLine();
            if (command.equals("0")) break;
            try {
                controller.getWeather(command, city);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println("Данные введены некорректно!");
            }
        }
    }
}
