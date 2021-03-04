package com.example.ant.dao;

import com.example.ant.dto.User;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public interface UserDao {
     boolean addUser(User user);
     boolean deleteUser(User user);
     boolean updateUser(User user);
     User selectUser(User user) throws ExecutionException, InterruptedException, SQLException;
}
