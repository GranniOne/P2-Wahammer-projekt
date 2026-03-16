package com.P2.warhammer.views;

import com.P2.warhammer.Skills.SkillsRepository;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;



@PermitAll
@PageTitle("available skills")
@Route("skills")
public class SkillsView extends Div implements BeforeEnterObserver {

    private final SkillsRepository skillsRepository;

    public SkillsView(SkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        VerticalLayout layout = new VerticalLayout();
        layout.getStyle().set("margin-left", "auto").setPadding("100px");
        skillsRepository.findAll().forEach(skill -> layout.add(new Span(String.valueOf(skill))));
        add(layout);
    }
}
