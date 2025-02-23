package com.ticketleader.domain.seat;

import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class SeatLockingService {

    @Inject
    private RedisClient redisClient;

    private StatefulRedisConnection<String, String> connection;

    private static final Duration LOCK_DURATION = Duration.ofSeconds(300);

    @PostConstruct
    public void init() {
        if (redisClient == null) {
            throw new IllegalStateException("RedisClient is null. Check your configuration.");
        }
        connection = redisClient.connect();
    }

    /**
     * Locks a seat for a given event and reservee.
     *
     * @param eventId    the event identifier.
     * @param seatId     the seat identifier.
     * @param reserveeId the reservee's identifier.
     */
    public boolean lockSeat(Long eventId, Long sectorId, Long seatId, Long reserveeId) {
        String key = "seatLock:" + eventId + ":" + sectorId + ":" + seatId;
        String result = connection
                .sync()
                .set(key, reserveeId.toString(), SetArgs.Builder.nx().ex(LOCK_DURATION));
        return result != null && result.equalsIgnoreCase("OK");
    }

    public boolean isSeatLockedBy(Long eventId, Long sectorId, Long seatId, Long reserveeId) {
        String key = "seatLock:" + eventId + ":" + sectorId + ":" + seatId;
        String value = connection.sync().get(key);
        return value != null && value.equals(reserveeId.toString());
    }

    public Set<Long> getLockedSeatIds(Long eventId) {
        String pattern = "seatLock:" + eventId + ":*";
        List<String> keys = connection.sync().keys(pattern);
        return keys.stream()
                .map(key -> Long.valueOf(key.replace("seatLock:" + eventId + ":", "")))
                .collect(Collectors.toSet());
    }
}
