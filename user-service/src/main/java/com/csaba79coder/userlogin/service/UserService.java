package com.csaba79coder.userlogin.service;

import com.csaba79coder.models.UserModel;
import com.csaba79coder.models.UserRegistrationModel;
import com.csaba79coder.userlogin.entity.User;
import com.csaba79coder.userlogin.persistence.UserRepository;
import com.csaba79coder.userlogin.util.Mapper;
import com.csaba79coder.userlogin.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private static final String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,32}$";

    public UserModel saveUser(UserRegistrationModel model) {
        Optional<User> user = userRepository.findByEmail(model.getEmail());
        if (user.isPresent()) {
            String message = String.format("User with email: %s is already exists", model.getEmail());
            log.info(message);
            throw  new NoSuchElementException(message);
        }
        if (!Validator.patternMatches(model.getPassword(), regex)) {
            String message = "Please provide a valid password";
            throw new InputMismatchException(message);
        }
        if(!model.getPassword().equals(model.getRepeatedPassword())) {
            String message = "Password and repeated password does not match";
            throw new InputMismatchException(message);
        } else {
            return Mapper.mapUserEntityToUserModel(userRepository.save(Mapper.mapUserRegModelToEntity(model)));
        }
    }

    public List<UserModel> renderAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(Mapper::mapUserEntityToUserModel)
                .collect(Collectors.toList());
    }
}
