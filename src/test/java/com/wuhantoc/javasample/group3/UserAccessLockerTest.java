package com.wuhantoc.javasample.group3;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.wuhantoc.javasample.group3.TextConstant.USER_STORE_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.TextConstant.USER_TAKE_OUT_FAIL_MESSAGE;
import static com.wuhantoc.javasample.group3.UserStoreResult.userStoreSuccess;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.platform.commons.util.StringUtils.isNotBlank;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserAccessLockerTest {

    private static final boolean FULL_LOCKER = true;
    private static final boolean AVAILABLE_LOCKER = false;

    protected Function<Boolean, UserAccessLocker> lockerProvider;

    @DisplayName("when_user_store")
    @ParameterizedTest(name = "{1}")
    @MethodSource("initUserStoreArgs")
    public void should_match_expected_result_when_user_store_given_provided_locker(Boolean fullLocker, UserStoreResult expectedResult) {
        //given
        UserAccessLocker locker = lockerProvider.apply(fullLocker);
        //when
        UserStoreResult result = locker.userStore();
        //then
        assertNotNull(result);
        assertEquals(expectedResult.isSuccess(), result.isSuccess());
        if (expectedResult.isSuccess()) {
            assertTrue(isNotBlank(result.getTicket()));
            assertNotNull(result.getLockerBox());
        } else {
            assertEquals(USER_STORE_FAIL_MESSAGE, result.getErrorMessage());
        }
    }

    @DisplayName("when_user_take_out")
    @ParameterizedTest(name = "{2}")
    @MethodSource("initUserTakeOutArgs")
    public void should_match_expected_result_when_user_take_out_given_provided_locker(Boolean fullLocker, Function<UserAccessLocker, UserStoreResult> storeResultProvider, UserTakeOutResult expectedResult) {
        UserAccessLocker locker = lockerProvider.apply(fullLocker);
        UserStoreResult storeResult = storeResultProvider.apply(locker);
        String ticketToTakeOut = storeResult.getTicket();
        //when
        UserTakeOutResult result = locker.userTakeOut(ticketToTakeOut);
        //then
        assertNotNull(result);
        assertEquals(expectedResult.isSuccess(), result.isSuccess());
        if (expectedResult.isSuccess()) {
            assertTrue(result.isSuccess());
            assertEquals(storeResult.getLockerBox(), result.getLockerBox());
        } else {
            assertEquals(USER_TAKE_OUT_FAIL_MESSAGE, result.getErrorMessage());
        }
    }

    private static Stream<Arguments> initUserStoreArgs() {
        return Stream.of(
                givenAvailableLockerThenSuccess(),
                givenFullLockerThenNotSuccess()
        );
    }

    static Stream<Arguments> initUserTakeOutArgs() {
        return Stream.of(
                givenCorrectTicketThenSuccess(),
                givenIncorrectTicketThenNotSuccess()
        );
    }

    private static Arguments givenAvailableLockerThenSuccess() {
        return Arguments.of(AVAILABLE_LOCKER, storeSuccess("available_locker"));
    }

    private static Arguments givenFullLockerThenNotSuccess() {
        return Arguments.of(FULL_LOCKER, storeFail("full_locker"));
    }

    private static Arguments givenCorrectTicketThenSuccess() {
        Function<UserAccessLocker, UserStoreResult> provider = UserAccessLocker::userStore;
        return Arguments.of(AVAILABLE_LOCKER, provider, takeOutSuccess("ticket_acquire_from_user_store"));
    }

    private static Arguments givenIncorrectTicketThenNotSuccess() {
        Function<UserAccessLocker, UserStoreResult> provider = locker -> {
            UserStoreResult storeResult = locker.userStore();
            String differentTicket;
            do {
                differentTicket = UUID.randomUUID().toString();
            } while (differentTicket.equals(storeResult.getTicket()));
            return userStoreSuccess(differentTicket, null);
        };
        return Arguments.of(AVAILABLE_LOCKER, provider, takeOutFail("different_from_any_ticket_from_store_result"));
    }

    private static UserStoreResult storeSuccess(String given) {
        return mockStoreResult(true, "should_get_a_success_result_given_" + given);
    }

    private static UserStoreResult storeFail(String given) {
        return mockStoreResult(false, "should_get_a_not_success_result_given_" + given);
    }

    private static UserStoreResult mockStoreResult(boolean success, String caseName) {
        UserStoreResult result = mock(UserStoreResult.class);
        when(result.isSuccess()).thenReturn(success);
        when(result.toString()).thenReturn(caseName);
        return result;
    }

    private static UserTakeOutResult takeOutSuccess(String given) {
        return mockTakeOutResult(true, "should_get_a_success_result_given_" + given);
    }

    private static UserTakeOutResult takeOutFail(String given) {
        return mockTakeOutResult(false, "should_get_a_not_success_result_given_" + given);
    }

    private static UserTakeOutResult mockTakeOutResult(boolean success, String caseName) {
        UserTakeOutResult result = mock(UserTakeOutResult.class);
        when(result.isSuccess()).thenReturn(success);
        when(result.toString()).thenReturn(caseName);
        return result;
    }

    protected static Supplier<UserRobotAccessLockerBox> mockLockerBoxSupplier() {
        return () -> mock(UserRobotAccessLockerBox.class);
    }

}
