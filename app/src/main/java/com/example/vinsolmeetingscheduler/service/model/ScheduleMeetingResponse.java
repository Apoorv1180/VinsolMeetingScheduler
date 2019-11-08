package com.example.vinsolmeetingscheduler.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleMeetingResponse implements Parcelable, Serializable {

@SerializedName("start_time")
@Expose
private String startTime;
@SerializedName("end_time")
@Expose
private String endTime;
@SerializedName("description")
@Expose
private String description;
@SerializedName("participants")
@Expose
private List<String> participants = null;

    protected ScheduleMeetingResponse(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        description = in.readString();
        participants = in.createStringArrayList();
    }

    public static final Creator<ScheduleMeetingResponse> CREATOR = new Creator<ScheduleMeetingResponse>() {
        @Override
        public ScheduleMeetingResponse createFromParcel(Parcel in) {
            return new ScheduleMeetingResponse(in);
        }

        @Override
        public ScheduleMeetingResponse[] newArray(int size) {
            return new ScheduleMeetingResponse[size];
        }
    };

    public String getStartTime() {
return startTime;
}

public void setStartTime(String startTime) {
this.startTime = startTime;
}

public String getEndTime() {
return endTime;
}

public void setEndTime(String endTime) {
this.endTime = endTime;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public List<String> getParticipants() {
return participants;
}

public void setParticipants(List<String> participants) {
this.participants = participants;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(description);
        parcel.writeStringList(participants);
    }
}