package com.wuhantoc.javasample.group3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wuhantoc.javasample.group3.RobotStoreResult.robotStoreFail;
import static com.wuhantoc.javasample.group3.RobotStoreResult.robotStoreSuccess;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.robotTakeOutFail;
import static com.wuhantoc.javasample.group3.RobotTakeOutResult.robotTakeOutSuccess;
import static com.wuhantoc.javasample.group3.Status.AVAILABLE;
import static com.wuhantoc.javasample.group3.Status.FULL;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.ROBOT_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.UserTakeOutResult.userTakeOutSuccess;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class LockerRobotTest {

    private static final Cargo ANY_CARGO = mock(Cargo.class);

    private static final String MOCK_TICKET = "";

    @DisplayName("when_user_store_cargo_to_robot")
    @ParameterizedTest(name = "{1}")
    @MethodSource("initStoreCargoArgs")
    protected void should_check_result_when_store_cargo_given_robot(Collection<Status> lockerStatuses, InnerResult expectedResult) {
        //given
        Collection<UserRobotAccessLocker> lockers = initLockers(lockerStatuses);
        LockerRobot robot = initRobotWithGivenLockers(lockers);
        //when
        RobotStoreCargoResult result = robot.storeCargo(ANY_CARGO);
        //then
        if (expectedResult.isSuccess()) {
            assertTrue(result.isSuccess());
            assertNotNull(result.getTicket());
            int index = 0;
            for (UserRobotAccessLocker locker : lockers) {
                if (index == expectedResult.getLockerIndex()) {
                    assertEquals(locker, result.getLocker());
                    break;
                }
                index++;
            }
        } else {
            assertFalse(result.isSuccess());
            assertEquals(ROBOT_STORE_FAIL_MESSAGE, result.getErrorMessage());
        }
    }

    @DisplayName("when_user_take_out_cargo_from_robot")
    @ParameterizedTest(name = "{2}")
    @MethodSource("initTakeOutCargoArgs")
    protected void should_expected_result_or_cargo_when_take_out_cargo_given_a_robot_and_ticket(Collection<Status> lockerStatuses, Function<LockerRobot, String> ticketProvider, InnerResult expectedResult) {
        //given
        Collection<UserRobotAccessLocker> lockers = initLockers(lockerStatuses);
        LockerRobot robot = initRobotWithGivenLockers(lockers);
        String ticket = ticketProvider.apply(robot);
        //when
        RobotTakeOutCargoResult result = robot.takeOutCargo(ticket);
        //then
        if (expectedResult.isSuccess()) {
            assertTrue(result.isSuccess());
            assertEquals(expectedResult.getCargo(), result.getCargo());
        } else {
            assertFalse(result.isSuccess());
            assertEquals(ROBOT_TAKE_OUT_FAIL_MESSAGE, result.getErrorMessage());
        }
    }

    protected final LockerRobot initRobotWithGivenLockers(Collection<UserRobotAccessLocker> lockers) {
        return new LockerRobot(lockers);
    }

    protected UserRobotAccessLocker initAvailableLocker() {
        String mockTicket = MOCK_TICKET;
        UserRobotAccessLockerBox lockerBox = mockLockerBox();
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        AtomicBoolean stored = new AtomicBoolean(false);
        when(locker.robotStore()).then(i -> {
            stored.set(true);
            return robotStoreSuccess(mockTicket, lockerBox);
        });
        when(locker.robotTakeOut(mockTicket)).then(i -> {
            if (stored.get()) {
                stored.set(false);
                return robotTakeOutSuccess(lockerBox);
            }
            return robotTakeOutFail(USER_TAKE_OUT_FAIL_MESSAGE);
        });
        when(locker.userTakeOut(mockTicket)).then(i -> {
            stored.set(false);
            return userTakeOutSuccess(lockerBox);
        });
        return locker;
    }

    protected UserRobotAccessLocker initFullLocker() {
        UserRobotAccessLocker locker = mock(UserRobotAccessLocker.class);
        when(locker.robotStore()).thenReturn(robotStoreFail(USER_STORE_FAIL_MESSAGE));
        return locker;
    }

    private static Stream<Arguments> initStoreCargoArgs() {
        return Stream.of(
                givenAvailableLocker1FullLocker2AndAvailableLocker3ThenStoreInLocker1(),
                givenFullLocker1AndFullLocker2ThenNotSuccess()
        );
    }

    private static Arguments givenAvailableLocker1FullLocker2AndAvailableLocker3ThenStoreInLocker1() {
        return Arguments.of(asList(AVAILABLE, FULL, AVAILABLE),
                InnerResult.storeSuccess(0, "available_locker1_full_locker2_and_available_locker3"));
    }

    private static Arguments givenFullLocker1AndFullLocker2ThenNotSuccess() {
        return Arguments.of(asList(FULL, FULL), InnerResult.storeFail("full_locker1_and_full_locker2"));
    }

    private static Stream<Arguments> initTakeOutCargoArgs() {
        return Stream.of(
                givenRobotAndTicketAcquireFromStore(),
                givenRobotAndDifferentTicket(),
                givenRobotAndUsedTicket()
        );
    }

    private static Arguments givenRobotAndTicketAcquireFromStore() {
        final Cargo cargo = mock(Cargo.class);
        Function<LockerRobot, String> ticketProvider = robot -> robot.storeCargo(cargo).getTicket();
        return Arguments.of(singletonList(AVAILABLE), ticketProvider,
                InnerResult.takeOutSuccess(cargo, "a_robot_and_ticket_acquire_from_store"));
    }

    private static Arguments givenRobotAndDifferentTicket() {
        final Cargo cargo = mock(Cargo.class);
        Function<LockerRobot, String> ticketProvider = robot -> {
            RobotStoreCargoResult result = robot.storeCargo(ANY_CARGO);
            String differentTicket;
            do {
                differentTicket = UUID.randomUUID().toString();
            } while (differentTicket.equals(result.getTicket()));
            return differentTicket;
        };
        return Arguments.of(singletonList(AVAILABLE), ticketProvider,
                InnerResult.takeOutFail("a_robot_and_ticket_different_from_any_ticket_from_store_cargo_result"));
    }

    private static Arguments givenRobotAndUsedTicket() {
        final Cargo cargo = mock(Cargo.class);
        Function<LockerRobot, String> ticketProvider = robot -> {
            RobotStoreCargoResult storeCargoResult = robot.storeCargo(cargo);
            String storeCargoTicket = storeCargoResult.getTicket();
            storeCargoResult.getLocker().userTakeOut(storeCargoTicket);
            return storeCargoTicket;
        };
        return Arguments.of(singletonList(AVAILABLE), ticketProvider,
                InnerResult.takeOutFail("a_robot_and_used_ticket"));
    }

    private Collection<UserRobotAccessLocker> initLockers(Collection<Status> lockerStatuses) {
        return lockerStatuses.stream().map(this::providerLocker).collect(Collectors.toList());
    }

    private UserRobotAccessLockerBox mockLockerBox() {
        UserRobotAccessLockerBox lockerBox = mock(UserRobotAccessLockerBox.class);
        ArgumentCaptor<Cargo> argCaptor = ArgumentCaptor.forClass(Cargo.class);
        doNothing().when(lockerBox).store(argCaptor.capture());
        when(lockerBox.get()).then(i -> argCaptor.getValue());
        return lockerBox;
    }

    private UserRobotAccessLocker providerLocker(Status status) {
        if (status == FULL) {
            return initFullLocker();
        }
        return initAvailableLocker();
    }

    @Getter
    @AllArgsConstructor
    @Builder
    private static class InnerResult {
        boolean success;
        int lockerIndex;
        Cargo cargo;
        String caseName;

        @Override
        public String toString() {
            return caseName;
        }

        static InnerResult storeSuccess(int lockerIndex, String given) {
            return builder()
                    .success(true)
                    .lockerIndex(lockerIndex)
                    .caseName("should_get_a_success_result_and_store_in_locker_" + (lockerIndex + 1) + "_given_" + given)
                    .build();
        }

        static InnerResult storeFail(String given) {
            return builder()
                    .success(false)
                    .caseName("should_get_a_not_success_result_given_" + given)
                    .build();
        }

        static InnerResult takeOutSuccess(Cargo cargo, String given) {
            return builder()
                    .success(true)
                    .cargo(cargo)
                    .caseName("should_get_stored_cargo_given_" + given)
                    .build();
        }

        static InnerResult takeOutFail(String given) {
            return builder()
                    .success(false)
                    .caseName("get_a_not_success_result_with_specified_error_message_given" + given)
                    .build();
        }

    }

}
