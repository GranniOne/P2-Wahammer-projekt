package com.P2.warhammer.views;

import com.P2.warhammer.campaigns.CampaignService;
import com.P2.warhammer.characters.Character;
import com.P2.warhammer.characters.CharacterService;
import com.P2.warhammer.users.User;
import com.P2.warhammer.users.UserService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;


@RolesAllowed("ROLE_ADMIN")
@Route("admin-dashboard")
@PageTitle("dashboard")
public class AdminDashboardView extends Div {
    final private UserService userService;
    final private CampaignService campaignService;
    final private CharacterService characterService;



    public AdminDashboardView(UserService userService, CampaignService campaignService, CharacterService characterService) {
        this.userService = userService;
        this.campaignService = campaignService;
        this.characterService = characterService;

        /*
        MultiSelectListBox<User> listBox = new MultiSelectListBox<>();
        List<User> users = userService.getAllUsers();

        listBox.setItems(users);
        listBox.select(users.getFirst(),users.getLast());
        add(listBox);

         */

        //Usercomponent component = new Usercomponent();
        //add(component);
        Grid<User> grid = new Grid<>();
        List<User> users = userService.getAllUsers();

        grid.setAllRowsVisible(true);
        grid.setItems(users);

        grid.addColumn(User::getUsername).setHeader("Username");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getPassword).setHeader("Passwords");
        grid.addComponentColumn(user -> new Button("nulstil adgangskode",event -> {
            openDialogResetPassword(user);
        })).setHeader("nulstil adgangskode");
        grid.addComponentColumn(user -> new Button("delete " + user.getUsername(), event -> {
            openDialogDeleteUser(user);
        })).setHeader("Delete user");
        grid.addComponentColumn(user -> new Button("Access Profile " + user.getUsername(), event -> {
            UI.getCurrent().navigate("admin-dashboard/UserProfile/"+ user.getId() +"/");

        })).setHeader("Access user");
        add(grid);
    }

    public void openDialogResetPassword(User user) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(
                String.format("nulstil user \"%s\"?", user.getUsername()));
        dialog.add("Are you sure you want reset this users password permanently?");


        Button deleteButton = new Button("nulstil", e -> {
            user.setPassword("1234abcd");
            userService.saveUser(user);
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(deleteButton);

        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);
        add(dialog);
        dialog.open();
    }


    public void openDialogDeleteUser(User user) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(
                String.format(" delete user \"%s\"?",user.getUsername()));
        dialog.add("Are you sure you want to delete this user permanently?");

        Button deleteButton = new Button("delete", e -> {
            campaignService.findbyPlayers(List.of(user)).forEach(campaign -> {
                List<User> users = campaign.getPlayers();
                users.removeIf(user1 ->  user1.getUsername().equals(user.getUsername()));
                campaign.setPlayers(users);

                List<Character> characters = campaign.getCharacters();
                characters.removeIf(character -> character.getUser().getId().equals(user.getId()));
                campaign.setCharacters(characters);


                campaignService.saveCampaign(campaign);
            });
            userService.deleteUser(user);
            campaignService.findByGameMaster(user).forEach(campaign -> {
                campaignService.deleteCampaignById(campaign.getId());

            });
            characterService.getCharactersByUser(user).forEach(character -> {
                characterService.deleteCharacterFromId(character.getId());
            });

            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-right", "auto");
        dialog.getFooter().add(deleteButton);

        Button cancelButton = new Button("Cancel", e -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(cancelButton);
        add(dialog);
        dialog.open();
    }



}



