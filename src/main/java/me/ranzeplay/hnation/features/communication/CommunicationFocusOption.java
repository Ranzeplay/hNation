package me.ranzeplay.hnation.features.communication;

public enum CommunicationFocusOption {
    GLOBAL,
    CHANNEL,
    DIRECT,
    SQUAD;

    private String commId;

    public String getCommId() {
        return commId;
    }

    public void setCommId(String commId) {
        this.commId = commId;
    }
}
