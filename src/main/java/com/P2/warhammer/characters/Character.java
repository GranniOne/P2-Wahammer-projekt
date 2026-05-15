package com.P2.warhammer.characters;

import com.P2.warhammer.Conditions.Condition;
import com.P2.warhammer.Skills.Skill;
import com.P2.warhammer.Talents.Talent;
import com.P2.warhammer.careers.Career;
import com.P2.warhammer.characteristics.Characteristic;
import com.P2.warhammer.items.Armour;
import com.P2.warhammer.items.WarhammerItem;
import com.P2.warhammer.items.Weapon;
import com.P2.warhammer.campaigns.Campaign;
import com.P2.warhammer.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents a game character in the system, stored in the "characters" collection in MongoDB.
 * Contains fields for character yet to be determined.
 *
 * <p>Provides constructors, getters, and setters for accessing and modifying character data.</p>

 */
@Document(collection = "characters")
public class Character {

    @Id
    private String id;
    String name;
    @DocumentReference
    User user;
    @DocumentReference
    User GameMaster;
    @DocumentReference
    Campaign campaign;

    String race;
    int age = 0;
    int experience = 0;
    int advantage = 0;
    int maxWounds = 0;
    int damageTaken = 0;
    int level = 1;
    List<Integer> armourValues = new ArrayList<>(); // I think we can just say that the first

    int corruptionMax = 0;
    int corruptionTaken = 0;

    int Pfennings = 0;
    int silverShillings = 0;
    int goldCrowns = 0;
    int statusLevel = 0; //your "class gold". see Excel sheet

    Career career;

    List<String> characteristicsNameArray = Arrays.asList(("Weapons Skill,Ballistic Skill,Strength,Toughness,Initiative,Agility,Dexterity,Intelligence,Willpower,Fellowship").split(","));

    List<WarhammerItem> inventory = new ArrayList<>();
    List<Armour> equippedArmour = new ArrayList<>();
    List<Weapon> equippedWeapons = new ArrayList<>();
    List<Talent> talents = new ArrayList<>();
    List<Skill> skills = new ArrayList<>();
    List<Condition> conditions = new ArrayList<>();
    List<Characteristic> characteristics; // will have this order: ws bs strength toughness initiative agility dexterity intelligence willpower fellowship


    public Character(String name, User user, User GameMaster, Integer age, Integer experience, List<Characteristic> characteristics, int level) {
        this.name = name;
        this.user = user;
        this.GameMaster = GameMaster;
        this.age = age;
        this.experience = experience;
        this.characteristics = characteristics;

    }

    public Character() {
        this.characteristics = new ArrayList<>();


        for (int i = 0; i < 10; i++){
            characteristics.add(new Characteristic(characteristicsNameArray.get(i),0,0,0,0));
        }

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getGameMaster() {
        return GameMaster;
    }

    public void setGameMaster(User gameMaster) {
        GameMaster = gameMaster;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getAdvantage() {
        return advantage;
    }

    public void setAdvantage(int advantage) {
        this.advantage = advantage;
    }

    public int getMaxWounds() {
        return maxWounds;
    }

    public void setMaxWounds(int maxWounds) {
        this.maxWounds = maxWounds;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public List<Integer> getArmourValues() {
        return armourValues;
    }

    public void setArmourValues(List<Integer> armourValues) {
        this.armourValues = armourValues;
    }

    public int getCorruptionMax() {
        return corruptionMax;
    }

    public void setCorruptionMax(int corruptionMax) {
        this.corruptionMax = corruptionMax;
    }

    public int getCorruptionTaken() {
        return corruptionTaken;
    }

    public void setCorruptionTaken(int corruptionTaken) {
        this.corruptionTaken = corruptionTaken;
    }

    public int getPfennings() {
        return Pfennings;
    }

    public void setPfennings(int pfennings) {
        Pfennings = pfennings;
    }

    public int getSilverShillings() {
        return silverShillings;
    }

    public void setSilverShillings(int silverShillings) {
        this.silverShillings = silverShillings;
    }

    public int getGoldCrowns() {
        return goldCrowns;
    }

    public void setGoldCrowns(int goldCrowns) {
        this.goldCrowns = goldCrowns;
    }

    public int getStatusLevel() {
        return statusLevel;
    }

    public void setStatusLevel(int statusLevel) {
        this.statusLevel = statusLevel;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public List<WarhammerItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<WarhammerItem> inventory) {
        this.inventory = inventory;
    }

    public List<Armour> getEquippedArmour() {
        return equippedArmour;
    }

    public void setEquippedArmour(List<Armour> equippedArmour) {
        this.equippedArmour = equippedArmour;
    }

    public List<Weapon> getEquippedWeapons() {
        return equippedWeapons;
    }

    public void setEquippedWeapons(List<Weapon> equippedWeapons) {
        this.equippedWeapons = equippedWeapons;
    }

    public List<Talent> getTalents() {
        return talents;
    }

    public void setTalents(List<Talent> talents) {
        this.talents = talents;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<Characteristic> characteristics) {
        this.characteristics = characteristics;
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
