package com.P2.warhammer.users;

import com.P2.warhammer.utilities.UserException;
import com.P2.warhammer.views.SignupView;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User addUser(String name, String email, String password) {
        return repository.save(new User(name, email, password));

    }
    public Boolean AuthenticateUser(String username, String email, String password, String confirmedPassword){
        String exception = "";

        if(username.isEmpty()){
            exception += "Username is empty";
        }
        if(email.isEmpty()){
            exception += "\nEmail is empty";
        }

        if(password.isEmpty()){
            exception += "\nPassword is empty ";
        }
        if(password.isEmpty()){
            exception += "\nConfirm password is empty ";
        }
        if(!password.equals(confirmedPassword)){
            exception += "\nThe password doesnt match ";
        }

        User user =  repository.findUserByEmail(email);
        Boolean test =
                username.isEmpty() ||
                email.isEmpty() ||
                password.isEmpty() ||
                confirmedPassword.isEmpty() ||
                (password.equals(confirmedPassword));



        if(user == null && !test){
                addUser(username, email, password);
                return true;
        }else{
            exception += "Email already registered";
        }
        Notification notification = Notification.show(exception);


        return false;

    }



    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
