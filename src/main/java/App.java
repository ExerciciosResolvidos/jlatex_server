/**
 * Created by luizamboni on 09/04/16.
 */
import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}
