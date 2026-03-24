package com.P2.warhammer.characters;

import com.P2.warhammer.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a game character in the system, stored in the "characters" collection in MongoDB.
 * Contains fields for character yet to be determined.
 *
 * <p>Provides constructors, getters, and setters for accessing and modifying character data.</p>

 */
@Document(collection = "characters")
@Getter
@Setter
public class Character {

    @Id
    private String id;
    String name;
    @DBRef
    User user;
    @DBRef
    User GameMaster;
    String race;
    int age = 0;

    int experience = 0;

    int advantage = 0;
    int maxWounds = 0;
    int damageTaken = 0;
    List<Integer> armourValues = new ArrayList<>(); // I think we can just say that the first

    int corruptionMax = 0;
    int corruptionTaken = 0;

    int Pfennings = 0;
    int silverShillings = 0;
    int goldCrowns = 0;
    int statusLevel = 0; //your "class gold". see Excel sheet

    List<Object> inventory = new ArrayList<>();
    List<Object> equippedArmour = new ArrayList<>();
    List<Object> equippedWeapons = new ArrayList<>();

    List<Object> talents = new ArrayList<>();
    List<Object> skills = new ArrayList<>();
    List<Object> conditions = new ArrayList<>();
    List<Integer> characteristics = new ArrayList<>(); // will have this order: ws bs strength toughness initiative agility dexterity intelligence willpower fellowship


    Character(String name, User user, User GameMaster) {
        this.name = name;
        this.user = user;
        this.GameMaster = GameMaster;

        //sets all armour values to zero
        for (int i = 1; i <= 5; i++) {
            armourValues.add(0);
        }
        for (int i = 1; i <= 10; i++) {
            characteristics.add(0);
        }

    }

    @Override
    public String toString() {
        return "Character{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", race='" + race + '\'' +
                ", age=" + age +
                ", experience=" + experience +
                ", advantage=" + advantage +
                ", maxWounds=" + maxWounds +
                ", damageTaken=" + damageTaken +
                ", armourValues=" + armourValues +
                ", corruptionMax=" + corruptionMax +
                ", corruptionTaken=" + corruptionTaken +
                ", Pfennings=" + Pfennings +
                ", silverShillings=" + silverShillings +
                ", goldCrowns=" + goldCrowns +
                ", statusLevel=" + statusLevel +
                ", inventory=" + inventory +
                ", equippedArmour=" + equippedArmour +
                ", equippedWeapons=" + equippedWeapons +
                ", talents=" + talents +
                ", skills=" + skills +
                ", conditions=" + conditions +
                ", characteristics=" + characteristics +
                '}';
    }
}
