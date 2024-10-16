package com.school.school.utils;

import com.school.school.dto.Role;
import com.school.school.dto.User;

public class TestData {



    public static User buildUser() {
        return new User(1L, "test firstname", "test lastname", "test-email2@yopmail.com", "pwd", Role.Student);
    }

}
