package sg.ncl.service.user.domain;

/**
 * The {@link UserStatus} enumerates the statuses of a {@link User}.
 *
 * @author Christopher Zhong
 */
public enum UserStatus {
    CREATED, PENDING, APPROVED, REJECTED, CLOSED, FROZEN
}
