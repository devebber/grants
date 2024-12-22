public class Grant {
    public final String companyName;
    public final String streetName;
    public final double grantAmount;
    public final int year;
    public final String businessType;
    public final int workplacesQuantity;
    public Grant(String companyName, String streetName, double grantAmount, int year, String businessType,
                  int workplacesQuantity){
        this.companyName = companyName;
        this.streetName = streetName;
        this.grantAmount = grantAmount;
        this.year = year;
        this.businessType = businessType;
        this.workplacesQuantity = workplacesQuantity;
    }
}
