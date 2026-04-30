package com.P2.warhammer.views;

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

    public AdminDashboardView(UserService userService) {
        this.userService = userService;
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
            openDialog(user,event.getSource().getText());
        })).setHeader("nulstil adgangskode");
        grid.addComponentColumn(user -> new Button("delete " + user.getUsername(), event -> {
            openDialog(user,"delete");
        })).setHeader("Delete user");
        grid.addComponentColumn(user -> new Button("Access Profile " + user.getUsername(), event -> {
            UI.getCurrent().navigate("admin-dashboard/UserProfile/"+ user.getId() +"/");

        })).setHeader("Access user");



        add(grid);
    }

    private void openDialog(User user,String option) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(
                String.format(" %s user \"%s\"?",option, user.getUsername()));
        dialog.add("Are you sure you want to delete this user permanently?");
        System.out.println(option);

        Button deleteButton = new Button(option, e -> {
            System.out.println(option);
            if(option.equals("delete")){

                userService.deleteUser(user);
            }
            if(option.equals("nulstil adgangskode")){
                user.setPassword("1234abcd");
                userService.saveUser(user);
            }

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



