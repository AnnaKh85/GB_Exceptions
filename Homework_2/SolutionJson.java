import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.StringJoiner;

public class SolutionJson {

    public static class TestJsonClass {
        private String colorName;
        private int wheelsCount;

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public int getWheelsCount() {
            return wheelsCount;
        }

        public void setWheelsCount(int wheelsCount) {
            this.wheelsCount = wheelsCount;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", TestJsonClass.class.getSimpleName() + "[", "]")
                    .add("colorName='" + colorName + "'")
                    .add("wheelsCount=" + wheelsCount)
                    .toString();
        }
    }

    //По желанию: Придумайте структуру класса. Опишите класс в программе, создайте json с несколькими экземплярами. В программе с помощью objectMapper преобразуйте json в массив объектов.
    private static void makeJson() {
        String json =
                "["+
                    "{\"colorName\": \"Красный\", \"wheelsCount\": \"18\"},"+
                    "{\"colorName\": \"Желтый\", \"wheelsCount\": \"4\"},"+
                    "{\"colorName\": \"Черный\", \"wheelsCount\": \"1\"}"+
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TestJsonClass[] array = objectMapper.readValue(json, TestJsonClass[].class);
            for (TestJsonClass t: array) {
                System.out.println(t);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] params) {
        makeJson();
    }

}
