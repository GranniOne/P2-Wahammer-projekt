package com.P2.warhammer.careers;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Career {
    @Id
    private String Id;
    String Name;


    /*  TODO: my thougths are that we can iterate trough list to get the characteristics. like the first 3 elements are the level one, and the fourth is level 2 and so on.
     *   i think we can do the same for the rest of important elements in the career
     *
     *  TODO: I think we should use strings for the characteristics, as we arent trying to save changing characteristic data in this class.
     *   this is only used as a blueprint for what things the players character should have access to as this "job"
     */


    List<String> levelCharacterticsList = new ArrayList<>();
    List<String> levelTrappingsList = new ArrayList<>();
    List<String> levelStatusList = new ArrayList<>();
    List<String> levelTalentsList = new ArrayList<>();
    List<String> levelSkillsList = new ArrayList<>();
    String socialClass = "";


    //TODO add name, status, social class, talents, skills, characteristics, trappings

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<String> getLevelCharacterticsList() {
        return levelCharacterticsList;
    }

    public void setLevelCharacterticsList(List<String> levelCharacterticsList) {
        this.levelCharacterticsList = levelCharacterticsList;
    }

    public List<String> getLevelTrappingsList() {
        return levelTrappingsList;
    }

    public void setLevelTrappingsList(List<String> levelTrappingsList) {
        this.levelTrappingsList = levelTrappingsList;
    }

    public List<String> getLevelStatusList() {
        return levelStatusList;
    }

    public void setLevelStatusList(List<String> levelStatusList) {
        this.levelStatusList = levelStatusList;
    }

    public List<String> getLevelTalentsList() {
        return levelTalentsList;
    }

    public void setLevelTalentsList(List<String> levelTalentsList) {
        this.levelTalentsList = levelTalentsList;
    }

    public List<String> getLevelSkillsList() {
        return levelSkillsList;
    }

    public void setLevelSkillsList(List<String> levelSkillsList) {
        this.levelSkillsList = levelSkillsList;
    }

    public String getSocialClass() {
        return socialClass;
    }

    public void setSocialClass(String socialClass) {
        this.socialClass = socialClass;
    }

    public Career(String id, String name, List<String> levelCharacterticsList, List<String> levelTrappingsList, List<String> levelStatusList, List<String> levelTalentsList, List<String> levelSkillsList, String socialClass) {
        Id = id;
        Name = name;
        this.levelCharacterticsList = levelCharacterticsList;
        this.levelTrappingsList = levelTrappingsList;
        this.levelStatusList = levelStatusList;
        this.levelTalentsList = levelTalentsList;
        this.levelSkillsList = levelSkillsList;
        this.socialClass = socialClass;
    }

}
