import model.Human;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by nastya on 24.04.16.
 */
public class App {

    public static void main(String[] args) {

        Human human = new Human("Alex", "Taylor");
        human.setHobby("football");
        Collection children = new ArrayList<Human>();
        children.add(new Human("Max", "Taylor"));
        human.setСhildren(children);
        human.setMarried(true);
        LocalDate birthDate = LocalDate.of(1970, 12, 12);
        human.setBirthDate(birthDate);
        System.out.println(new JsonSerializer().serialize(human));
    }

}