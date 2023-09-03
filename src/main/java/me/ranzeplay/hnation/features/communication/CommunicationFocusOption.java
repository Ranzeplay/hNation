package me.ranzeplay.hnation.features.communication;

public enum CommunicationFocusOption {
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
