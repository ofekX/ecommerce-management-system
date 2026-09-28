public class Address {
    private String stateName;
    private String cityName;
    private String streetName;
    private int buildingNum;

    public Address(String stateName, String cityName, String streetName, int buildingNum) {
        this.stateName = stateName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.buildingNum = buildingNum;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }
}
