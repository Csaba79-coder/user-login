package com.csaba79coder.userlogin.util;

import com.csaba79coder.models.UserModel;
import com.csaba79coder.models.UserRegistrationModel;
import com.csaba79coder.userlogin.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static User mapUserRegModelToEntity(UserRegistrationModel model) {
        User user = new User();
        user.setEmail(model.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(model.getPassword()));
        return user;
    }

    public static UserModel mapUserEntityToUserModel(User user) {
        UserModel model = new UserModel();
        modelMapper.map(user, model);
        return model;
    }

    private Mapper() {

    }
}
