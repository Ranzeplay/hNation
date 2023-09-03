package me.ranzeplay.hnation.client;

public enum ChatFocus {
    PUBLIC,
    CHANNEL,
    PRIVATE,
    SQUAD;

    String commId;

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }
}
