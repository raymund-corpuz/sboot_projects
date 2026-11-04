package com.example.studentmanagement.security;


import com.example.studentmanagement.exception.TooManyLoginAttemptsException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginAttemptService {

    private static final int MAX_FAILURES = 5;
    private static final Duration WINDOW = Duration.ofMinutes(15);
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);
    private static final int MAX_TRACKED_KEYS = 10_000;

    private record Attempt(int failures, Instant windowStart, Instant lockedUntil){
        //some code..
    }

    private final ConcurrentHashMap<String, Attempt> attempts = new ConcurrentHashMap<>();

    public void checkAllowed(String key){
        Attempt attempt = attempts.get(key);

        if(attempt != null && attempt.lockedUntil() != null){
            Instant now = Instant.now();

            if(now.isBefore(attempt.lockedUntil())){
                throw new TooManyLoginAttemptsException(
                        Duration.between(now, attempt.lockedUntil()).toSeconds() + 1
                );
            }
            attempts.remove(key, attempt);
        }
    }

    public void recordFailure(String key){

        Instant now = Instant.now();
        if(attempts.size() > MAX_TRACKED_KEYS){
            evictExpired(now);
        }

        attempts.compute(key, (k, current) ->{
            if(current == null || now.isAfter(current.windowStart().plus(WINDOW))){
                return new Attempt(1, now, null);
            }

            int failures = current.failures() + 1;
            Instant lockedUntil = failures >= MAX_FAILURES
                    ?now.plus(LOCK_DURATION) : current.lockedUntil();
            return new Attempt(failures, current.windowStart(), lockedUntil);
        });
    }

    public void recordSuccess(String key){
        attempts.remove(key);
    }

    private void evictExpired(Instant now){
        attempts.entrySet().removeIf(entry -> {
            Attempt a =entry.getValue();

            boolean windowOver = now.isAfter(a.windowStart().plus(WINDOW));
            boolean unlocked =  a.lockedUntil() == null || now.isAfter(a.lockedUntil());
            return windowOver && unlocked;
        });
    }
}
