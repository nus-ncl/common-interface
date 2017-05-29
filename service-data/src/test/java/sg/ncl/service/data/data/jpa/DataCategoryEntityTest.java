package sg.ncl.service.data.data.jpa;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by dcsjnh on 11/5/2017.
 */
public class DataCategoryEntityTest {

    @Test
    public void testGetId() {
        DataCategoryEntity entity = new DataCategoryEntity();
        assertThat(entity.getId()).isNull();
    }

    @Test
    public void testSetId() {
        DataCategoryEntity entity = new DataCategoryEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(10));
        entity.setId(id);
        assertThat(entity.getId()).isEqualTo(id);
    }

    @Test
    public void testGetName() {
        DataCategoryEntity entity = new DataCategoryEntity();
        assertThat(entity.getName()).isNull();
    }

    @Test
    public void testSetName() {
        DataCategoryEntity entity = new DataCategoryEntity();
        String name = RandomStringUtils.randomAlphanumeric(20);
        entity.setName(name);
        assertThat(entity.getName()).isEqualTo(name);
    }

    @Test
    public void testGetDescription() {
        DataCategoryEntity entity = new DataCategoryEntity();
        assertThat(entity.getDescription()).isNull();
    }

    @Test
    public void testSetDescription() {
        DataCategoryEntity entity = new DataCategoryEntity();
        String description = RandomStringUtils.randomAlphanumeric(20);
        entity.setDescription(description);
        assertThat(entity.getDescription()).isEqualTo(description);
    }

}
