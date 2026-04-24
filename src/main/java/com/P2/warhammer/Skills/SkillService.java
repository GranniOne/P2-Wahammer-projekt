package com.P2.warhammer.Skills;

import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class SkillService {
    private final SkillRepository repository;

    public SkillService(SkillRepository repository) {
        this.repository = repository;
    }

    public Skill addSkill(String Name, String Category, String Characteristic, String Description, Integer startValue, Integer bonusValue,  Integer penaltyValue) {
        return repository.save(new Skill(Name, Category, Characteristic, Description,  startValue, bonusValue, penaltyValue));

    }

    public List<Skill> getAllSkills() {
        return repository.findAll();
    }
}
