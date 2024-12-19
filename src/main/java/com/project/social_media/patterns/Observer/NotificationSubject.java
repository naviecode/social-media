package com.project.social_media.patterns.Observer;

import com.project.social_media.dto.NotificationsDto;


import java.util.ArrayList;
import java.util.List;

public class NotificationSubject {
    private List<UserObserver> observers = new ArrayList<>();

    public void addObserver(UserObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(UserObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(NotificationsDto notify) {
        List<UserObserver> observersCopy = new ArrayList<>(observers);
        for (UserObserver observer : observersCopy) {
            observer.update(notify);
        }
    }
}
