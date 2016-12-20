package sg.ncl.service.analytic.domain;

import java.time.ZonedDateTime;

/**
 *  The {@link DataSet} interface represents a data set.
 *  Author: Vu
 */
public interface DataSet {


        /**
         * Returns the id of this {@link DataSet}.
         *
         * @return the id of this {@link DataSet}.
         */
        Long getId();

        /**
         * Returns the name of this {@link DataSet}.
         *
         * @return the name of this {@link DataSet}.
        */
        String getName();

        /**
         * Returns the description of this {@link DataSet}.
         *
         * @return the description of this {@link DataSet}.
         */
        String getDescription();

        /**
         * Returns the accesssibility of this {@link DataSet}.
         *
         * @return the accesssibility of this {@link DataSet}.
         */
        String getAccessibility();

        /**
         * Returns the visibility of this {@link DataSet}.
         *
         * @return the visibility of this {@link DataSet}.
         */
        String getVisibility();


        /**
         * Returns the contributor id of this {@link DataSet}.
         *
         * @return the ontributor idof this {@link DataSet}.
         */
        String getContributorId();


    }
