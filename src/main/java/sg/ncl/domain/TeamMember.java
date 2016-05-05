package sg.ncl.domain;

/**
 * The {@link TeamMember} interface represents a {@link User} that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
public interface TeamMember {

    /**
     * Returns the associated {@link Team}.
     *
     * @return the associated {@link Team}.
     */
    Team getTeam();

    /**
     * Returns the associated {@link User}.
     *
     * @return the associated {@link User}.
     */
    User getUser();

}
