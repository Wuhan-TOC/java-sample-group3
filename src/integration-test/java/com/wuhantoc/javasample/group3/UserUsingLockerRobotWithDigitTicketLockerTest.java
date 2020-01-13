package com.wuhantoc.javasample.group3;

import com.wuhantoc.javasample.group3.impl.SimpleCargo;
import com.wuhantoc.javasample.group3.impl.SimpleUserAccessLockerBox;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Supplier;

import static com.wuhantoc.javasample.group3.impl.UserSuperRobotLockerWithCustomDigitTicket.initLocker;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserUsingLockerRobotWithDigitTicketLockerTest extends LockerRobotTest {

    @Test
    void should_each_user_take_out_the_stored_cargo_times_when_30_users_store_cargo_first_given_robot_with_1_digit_10_capacity_locker1_1_digit_10_capacity_locker2__1_digit_10_capacity_locker3() {
        int times = 30;
        UserTicketCargoEntry[] ticketCargoEntries = new UserTicketCargoEntry[times];
        for (int i = 0; i < times; i++) {
            ticketCargoEntries[i] = new UserTicketCargoEntry();
            ticketCargoEntries[i].cargo = new SimpleCargo();
        }
        Map<String, Cargo> u;
        LockerRobot robot = initRobotWithGivenLockers(asList(
                initLocker(10, 1, simpleLockerBoxSupplier()),
                initLocker(10, 1, simpleLockerBoxSupplier()),
                initLocker(10, 1, simpleLockerBoxSupplier())
        ));
        for (int i = 0; i < times; i++) {
            RobotStoreCargoResult result = robot.storeCargo(ticketCargoEntries[i].cargo);
            assertTrue(result.isSuccess());
            assertNotNull(result.getLockerTicket());
            assertNotNull(result.getRobotTicket());
            ticketCargoEntries[i].ticket = result.getRobotTicket();
        }
        for (UserTicketCargoEntry entry : ticketCargoEntries) {
            RobotTakeOutCargoResult result = robot.takeOutCargo(entry.ticket);
            assertTrue(result.isSuccess());
            assertEquals(entry.cargo, result.getCargo());
        }
    }

    @Override
    protected UserRobotAccessLocker initAvailableLocker() {
        return initLocker(10, 4, simpleLockerBoxSupplier());
    }

    @Override
    protected UserRobotAccessLocker initFullLocker() {
        return initLocker(0, 4, simpleLockerBoxSupplier());
    }

    protected static Supplier<UserRobotAccessLockerBox> simpleLockerBoxSupplier() {
        return SimpleUserAccessLockerBox::new;
    }

    private static class UserTicketCargoEntry {
        String ticket;
        Cargo cargo;
    }

}
