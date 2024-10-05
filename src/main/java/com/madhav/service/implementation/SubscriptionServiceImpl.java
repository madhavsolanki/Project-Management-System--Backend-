package com.madhav.service.implementation;

import com.madhav.entities.PlanType;
import com.madhav.entities.Subscription;
import com.madhav.entities.User;
import com.madhav.repository.SubscriptionRepository;
import com.madhav.service.interfaces.SubscriptionService;
import com.madhav.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(User user) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubscriptionStartDate(LocalDate.now());
        subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlantype(PlanType.FREE);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getUserSubscription(Long userId) throws Exception {
        Subscription subscription =  subscriptionRepository.findByUserId(userId);

        if (!isValid(subscription)){
            subscription.setPlantype(PlanType.FREE);
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
            subscription.setSubscriptionStartDate(LocalDate.now());
        }

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription upgrdeSubscription(Long userId, PlanType planType) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        subscription.setPlantype(planType);
        subscription.setSubscriptionStartDate(LocalDate.now());
        if (planType.equals(PlanType.ANNUALLY)){
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(12));
        }else {
            subscription.setSubscriptionEndDate(LocalDate.now().plusMonths(1));
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public boolean isValid(Subscription subscription) {
        if (subscription.getPlantype().equals(PlanType.FREE)){
            return true;
        }
        LocalDate endDate = subscription.getSubscriptionEndDate();
        LocalDate currentDate = LocalDate.now();

        return endDate.isAfter(currentDate) || endDate.isEqual(currentDate);
    }
}
