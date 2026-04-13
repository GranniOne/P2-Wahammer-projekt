package com.P2.warhammer.careers;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class Career {
    @Id
    private String Id;
    String Name;

    List<String> levelOneCharacterticsList = new ArrayList<>();



    //TODO add name, status, class, talents, skills, characteristics, trappings

    public Career(){






    }
}
