import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserDataRegistry {
    private static final int CODE_OVERAGE_DATA = 100;
    private static final int CODE_SHORTAGE_DATA = -100;

    public static class ParseLengthException extends Exception {
        public final int code;

        public ParseLengthException(int code) {
            super();
            this.code = code;
        }
    }

    public static class FileException extends Exception {
        public final String filePath;

        public FileException(String filePath, String message) {
            super(message);
            this.filePath = filePath;
        }
    }

    public static class ListElem {
        public String value;
        public boolean inUse;

        public ListElem(String value) {
            this.value = value;
            inUse = false;
        }
    }

    public static class UserData {
        public String gender;
        public String firstName;//Имя
        public String middleName;
        public String lastName; //Фамилия
        public String birthdate;
        public Long phoneNumber;
    }

    //Фамилия Имя Отчество датарождения номертелефона пол
    public static UserData parse(String text) throws ParseLengthException {
        final int DATA_COUNT_EXACT = 6;
        final String DATE_FORMAT = "dd.MM.yyyy";
        final String PATTERN_DATE = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        final String PATTERN_GENDER = "^(f|m)$";
        final String PATTERN_PHONE = "^\\d{1,10}$";

        UserData result = new UserData();

        //1 Создать массив
        String[] arr = text.split("\\s+");
        List<ListElem> list = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].trim().isEmpty()) continue;
            list.add(new ListElem(arr[i].trim()));
        }

        if (list.size() < DATA_COUNT_EXACT) {
            throw new ParseLengthException(CODE_SHORTAGE_DATA);
        }
        if (list.size() > DATA_COUNT_EXACT) {
            throw new ParseLengthException(CODE_OVERAGE_DATA);
        }

        //2 Атрибуты, но не ФИО
        for (ListElem le: list) {
            if (le.inUse) continue;

            String s = le.value;

            if (Pattern.matches(PATTERN_GENDER, s)) {
                if (result.gender == null) {
                    result.gender = s;
                    le.inUse = true;
                } else {
                    throw new RuntimeException("Повторно указан пол:"+ s);
                }
            } else
            if (Pattern.matches(PATTERN_DATE, s)) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                    Date d = sdf.parse(s);
                    if (result.birthdate == null) {
                        result.birthdate = s;
                        le.inUse = true;
                    } else {
                        throw new RuntimeException("Повторно указана дата рождения:"+ s);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException("Неверный формат даты рождения:"+ s);
                }
            } else
            if (Pattern.matches(PATTERN_PHONE, s)) {
                if (result.phoneNumber == null) {
                    result.phoneNumber = Long.parseLong(s);
                    le.inUse = true;
                } else {
                    throw new RuntimeException("Повторно указан номер телефона: "+ s);
                }
            }
        }

        if (result.phoneNumber == null) {
            throw new RuntimeException("Не указан телефон");
        }
        if (result.birthdate == null) {
            throw new RuntimeException("Не указана дата рождения");
        }
        if (result.gender == null) {
            throw new RuntimeException("Не указан пол");
        }

        //3 ФИО
        List<String> triple = new ArrayList<>(3);
        if (list.size()-2 > 0) {
            for (int i = 0; i < list.size()-2; i++) {
                ListElem le = list.get(i);
                if (le.inUse) {
                    triple.clear();
                    continue;
                } else {
                    triple.add(le.value);
                    if (triple.size() == 3) break;
                }
            }
        }
        if (triple.size() != 3) {
            throw new RuntimeException("Не определены Фамилия Имя Отчество");
        } else {
            result.lastName = triple.get(0);
            result.firstName = triple.get(1);
            result.middleName = triple.get(2);
        }

        //4
        if (result.firstName == null || result.middleName == null || result.lastName == null) {
            throw new RuntimeException("Некорректно указаны ФИО");
        }

        return result;
    }

    public static void saveToFile(UserData userData, String FILE_DIR) throws FileException {
        if (userData == null || userData.lastName == null || userData.lastName.isEmpty()) {
            throw new FileException(null, "Сохранение невозможно: отсутстует фамилия");
        }

        String fileName = FILE_DIR + userData.lastName + ".udreg";
        File f = new File(fileName);
        try (
                FileWriter fw = new FileWriter(f, true);
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            //<Фамилия><Имя><Отчество><датарождения><номертелефона><пол>
            String s = String.format("<%s><%s><%s><%s><%d><%s>",
                    userData.lastName, userData.firstName, userData.middleName,
                    userData.birthdate,
                    userData.phoneNumber,
                    userData.gender
            );
            bw.append(s);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(fileName, e.getMessage());
        }
        System.out.println("файл:" + f.getAbsolutePath());
    }

    public static void main(String[] params) {
        final String FILE_DIR = ""; //test
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите следующие данные в произвольном порядке, разделенные пробелом:");
            System.out.println("Фамилия Имя Отчество датарождения номертелефона пол");
            System.out.println("...дата_рождения - строка формата dd.mm.yyyy");
            System.out.println("...номер_телефона - целое беззнаковое число без форматирования");
            System.out.println("...пол - символ латиницей f или m");
            System.out.println("0 - Выход");
            System.out.print("-->");

            String text = scanner.nextLine().trim();
            if (text.equals("0") || text.equalsIgnoreCase("o") || text.equalsIgnoreCase("о")) break;
            try {
                UserData userData = parse(text);
                saveToFile(userData, FILE_DIR);
                System.out.println("*****Сохранено!");
            } catch (ParseLengthException e) {
                 if (e.code == CODE_SHORTAGE_DATA) {
                     System.out.println("Недостаточно данных для записи");
                 } else
                 if (e.code == CODE_OVERAGE_DATA) {
                     System.out.println("Избыточно данных для записи");
                 } else {
                     System.out.println("Неизвестный код ошибки ["+ e.code +"]. Обратитесь к администратору системы");
                 }
            } catch (FileException e) {
                System.out.println("Ошибка записи в файл"+ (e.filePath == null ? ":" : " ["+ e.filePath +"]:") + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ошибка:" + e.getMessage());
            }
        }
    }

}
