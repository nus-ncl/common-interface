package sg.ncl.testbed_interface.domain;

/**
 * The {@link TeamMember} interface represents a {@link User} that is part of a {@link Team}.
 *
 * @author Christopher Zhong
 */
public interface TeamMember {

    Team getTeam();

    User getUser();

}
