package com.pierre44.go4lunch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pmeignen on 18/11/2021.
 */
public class ResultDistanceMatrix {
    @SerializedName("status")
    public String status;

    @SerializedName("rows")
    public List<InfoDistanceMatrix> rows;

    public class InfoDistanceMatrix {
        @SerializedName("elements")
        public List elements;

        public class DistanceElement {
            @SerializedName("status")
            public String status;
            @SerializedName("duration")
            public ValueItem duration;
            @SerializedName("distance")
            public ValueItem distance;
        }

        public class ValueItem {
            @SerializedName("value")
            public long value;
            @SerializedName("text")
            public String text;
        }
    }
}
