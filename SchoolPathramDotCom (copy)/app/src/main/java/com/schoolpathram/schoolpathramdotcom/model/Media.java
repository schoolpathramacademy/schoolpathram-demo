package com.schoolpathram.schoolpathramdotcom.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "media")
public class Media implements Serializable {
    @PrimaryKey()
    @NonNull
    private String id;

    private int serverId;

    @ColumnInfo(name = "media_url")
private String mediaUrl;
    @ColumnInfo(name = "media_type")
private String type;

    public Media(String id, String mediaUrl, String type) {
        this.id = id;
        this.mediaUrl = mediaUrl;
        this.type = type;
    }
    public Media(int serverId, String mediaUrl, String type) {
        this.id = UUID.randomUUID().toString();
        this.serverId = serverId;
        this.mediaUrl = mediaUrl;
        this.type = type;
    }

    public Media() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
