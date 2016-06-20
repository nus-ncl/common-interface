package sg.ncl.service.user.domain;

/**
 * The {@link Address} interface represents a typical physical address.
 *
 * @author Christopher Zhong
 */
public interface Address {

    /**
     * Returns the first line of this {@link Address}.
     *
     * @return the first line of this {@link Address}.
     */
    String getAddress1();

    /**
     * Returns the second line of this {@link Address}.
     *
     * @return the second line of this {@link Address}.
     */
    String getAddress2();

    /**
     * Returns the country of this {@link Address}.
     *
     * @return the country of this {@link Address}.
     */
    String getCountry();

    /**
     * Returns the region of this {@link Address}.
     *
     * @return the region of this {@link Address}.
     */
    String getRegion();

    String getCity();

    /**
     * Returns the zip code of this {@link Address}.
     *
     * @return the zip code of this {@link Address}.
     */
    String getZipCode();

}
