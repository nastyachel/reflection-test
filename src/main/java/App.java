import model.Cat;
import model.Human;
import model.Pet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nastya on 24.04.16.
 */
public class App {

    public static void main(String[] args) {

        Human human = new Human();
        human.setFirstName("Ivan");
        human.setLastName("Ivanov");
        human.setHobby("football");
        List<Pet> pets = new ArrayList<Pet>();
        pets.add(new Cat("Tom", "male"));
        pets.add(new Cat("Bitch", "female"));
        String[] children = {"boy", "girl"};
        human.setСhildren(children);
        human.setMarried(true);
        human.setPets(pets);
        LocalDate birthDate = LocalDate.of(1970, 12, 19);
        human.setBirthDate(birthDate);

        System.out.println(new JsonSerializer().serialize(human));
    }

}