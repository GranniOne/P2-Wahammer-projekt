package com.P2.warhammer.Talents;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TalentService {
    private final TalentRepository repository;

    public TalentService(TalentRepository repository) {
        this.repository = repository;
    }

    public Talent addTalent(String Name, String Category, String Characteristic, String Description) {
        return repository.save(new Talent(Name, Category, Characteristic, Description));

    }

    public List<Talent> getAllTalents() {
        return repository.findAll();
    }
}
