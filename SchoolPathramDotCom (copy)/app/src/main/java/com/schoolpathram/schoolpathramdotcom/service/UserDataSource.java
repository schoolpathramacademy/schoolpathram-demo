package com.schoolpathram.schoolpathramdotcom.service;

import com.schoolpathram.schoolpathramdotcom.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface UserDataSource {

    /**
     * Gets the user from the data source.
     *
     * @return the user from the data source.
     */
    Flowable<User> getUser();

    /**
     * Inserts the user into the data source, or, if this is an existing user, updates it.
     *
     * @param user the user to be inserted or updated.
     */
    Completable insertOrUpdateUser(User user);

    /**
     * Deletes all users from the data source.
     */
    void deleteAllUsers();

    /**
     * Gets the user from the data source.
     *
     * @return the user from the data source.
     */
    Flowable<User> login(String username, String password);

}