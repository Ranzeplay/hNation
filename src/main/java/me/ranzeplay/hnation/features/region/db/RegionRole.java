package me.ranzeplay.hnation.features.region.db;

public enum RegionRole {
    NORMAL(0),
    STORAGE_POOL(1),
    TRANSIT_NODE(2),
    FACILITY(3),;

    RegionRole(int roleIndex) {}
}
