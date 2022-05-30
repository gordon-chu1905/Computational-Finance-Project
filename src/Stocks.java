import java.math.BigDecimal;
import java.util.List;

public class Stocks {

    //variables
    private double weight;
    private String name;
    private int startDate;
    private int endDate;
    private double highest;
    private double lowest;
    private double returnRate;
    private double riskRate;
    private List<Double> riskTime;
    private double sharpeRatio;
    private double optimizedReturn;
    private double optimizedRisk;
    private double optimizedCovariance;

    //constructor
    public Stocks(String name){
        this.name = name;
    }

    //functions
    public void addData(int startDate, int endDate, double highest, double lowest, double returnRate, double riskRate, List<Double> riskTime){
        this.startDate = startDate;
        this.endDate = endDate;
        this.highest = highest;
        this.lowest = lowest;
        this.returnRate = returnRate;
        this.riskRate = riskRate;
        this.riskTime = riskTime;
    }

    public void addWeight(double weight){
        this.weight = weight;
    }

    //Get variables
    public double getWeight() {
        return weight;
    }


    public String getName() {
        return name;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public double getHighest() {
        return highest;
    }

    public double getLowest() {
        return lowest;
    }

    public double getReturnRate() {
        return returnRate;
    }

    public double getRiskRate() {
        return riskRate;
    }

    public List<Double> getRiskTime() {
        return riskTime;
    }

    public double getSharpeRatio() {
        return sharpeRatio;
    }

    public double getOptimizedReturn() {
        return optimizedReturn;
    }

    public double getOptimizedRisk() {
        return optimizedRisk;
    }

    public double getOptimizedCovariance() {
        return optimizedCovariance;
    }

    public void addOptimizedRisk(double optimizedRisk){
        this.optimizedRisk = optimizedRisk;
    }

    public void addOptimizedReturn(double optimizedReturn){
        this.optimizedReturn = optimizedReturn;
    }

    public void addSharpeRatio(double sharpeRatio){
        this.sharpeRatio = sharpeRatio;
    }

    public void addOptimizedCovariance(double optimizedCovariance){
        this.optimizedCovariance =  optimizedCovariance;
    }
}
