package com.example.lazabee.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.lazabee.data.model.User;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(User user);
    
    @Update
    Completable updateUser(User user);
    
    @Delete
    Completable deleteUser(User user);
    
    @Query("SELECT * FROM users WHERE id = :userId")
    Single<User> getUserById(int userId);
    
    @Query("SELECT * FROM users WHERE email = :email")
    Single<User> getUserByEmail(String email);
    
    @Query("SELECT * FROM users WHERE username = :username")
    Single<User> getUserByUsername(String username);
    
    @Query("DELETE FROM users")
    Completable deleteAllUsers();
}