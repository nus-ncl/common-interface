package sg.ncl.service.data.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import sg.ncl.service.data.domain.DataCategory;

/**
 * Created by dcsjnh on 11/5/2017.
 */
@Getter
public class DataCategoryInfo implements DataCategory {

    private Long id;
    private String name;
    private String description;

    @JsonCreator
    public DataCategoryInfo(
            @JsonProperty("id") final Long id,
            @JsonProperty("name") final String name,
            @JsonProperty("description") final String description
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public DataCategoryInfo(DataCategory dataCategory) {
        this(
                dataCategory.getId(),
                dataCategory.getName(),
                dataCategory.getDescription()
        );
    }

}
