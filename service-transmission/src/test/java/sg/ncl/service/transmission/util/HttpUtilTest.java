package sg.ncl.service.transmission.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by huangxian on 4/12/16.
 */
public class HttpUtilTest {

    @Test
    public void testToLongIsEmpty() {
        long num = HttpUtils.toLong("", -1);
        assertThat(num).isEqualTo(-1);
    }

    @Test
    public void testToLongIsValue() {
        long num = HttpUtils.toLong("1", -1);
        assertThat(num).isEqualTo(1);
    }

    @Test
    public void testToLongValueNotNumber() {
        long num = HttpUtils.toLong("a", -1);
        assertThat(num).isEqualTo(-1);
    }

    @Test
    public void testToIntIsEmpty() {
        long num = HttpUtils.toInt("", -1);
        assertThat(num).isEqualTo(-1);
    }

    @Test
    public void testToIntIsValue() {
        long num = HttpUtils.toInt("1", -1);
        assertThat(num).isEqualTo(1);
    }

    @Test
    public void testToIntValueNotNumber() {
        long num = HttpUtils.toInt("a", -1);
        assertThat(num).isEqualTo(-1);
    }

}
