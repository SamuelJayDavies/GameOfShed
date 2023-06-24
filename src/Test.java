import java.util.ArrayList;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        Player sam = new Player("Sam", false);
        Player john = new Player("John", true);
        Shed testGame = new Shed(new ArrayList<Player>(Arrays.asList(sam, john)));
    }
}
