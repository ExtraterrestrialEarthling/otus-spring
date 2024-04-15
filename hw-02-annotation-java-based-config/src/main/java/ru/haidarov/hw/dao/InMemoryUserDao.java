package ru.haidarov.hw.dao;

import org.springframework.stereotype.Repository;
import ru.haidarov.hw.domain.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryUserDao implements UserDao {

    private final List<User> users;

    public InMemoryUserDao(List<User> users) {
        this.users = new ArrayList<>();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
