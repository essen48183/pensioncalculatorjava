//import java.util.Scanner;
/**
 *
 * @author Essen Davis
 * @version 1.0
 *  This program calculates the pension amount and required payments into system for an individual based on assumptions
 *
 */

public class PensionCalculatorPaymentsInto    {
    public static void main(String[] args) {

        /*
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter present value: ");
        double p=sc.nextDouble();
        System.out.print("Enter the interest rate: ");
        double r=sc.nextDouble();
        System.out.print("Enter the time period in years: ");
        double y=sc.nextDouble();
        */
        //test values
        double p = 10000;
        double r = 0.05;
        double y = 10;

        System.out.println("p = 10000, r = 0.05, y = 10");
        System.out.println("test Convert back and forth and assure lossless accuracy: ");

        double f=p*Math.pow((1+r/100),y);
        System.out.println("Future value is: "+f);
               //PV = FV / (1 + r / n)nt
        double pV = presentValue(f, r, y);
        double fV = futureValue(pV, r, y);

        System.out.println("Present value is: "+pV);
        System.out.println("Future value is: "+fV);
        System.out.println(" ");

        System.out.println("test calculateDiscountPayment on present value of 10000, 10 years work, 10 retired, employee makes 20000, contributes total of 1000, 7 percent interest, 2 " +
                "percent inflation: "+ calculateDiscountPayment(true,10000,0,1000, 20000,7,2,10, 10, 1 ));


    }



    /**
     * calculates the total  payment needed to reach a desired sum at the end of a given number of years
     * @param sumDesiredAtRetirement desired sum in todays dollars at end of investment
     * @param initialBalance initial balance in todays dollars
     * @param expectedInterestRate Annual interest rate
     * @param expectedInflationRate Annual inflation rate
     * @param yearsInvesting years investing...to get number of compounding periods (n = yearsInvested * compounding periods per year)
     * @param yearsRetired years retired.
     * @param compoundsPerYear number of compounding periods per year
     * @return presentValueOfAnnualPayments total payment in today's dollars needed to reach desired sum
     */

    public static double calculateDiscountPayment(boolean verbose, double sumDesiredAtRetirement, double initialBalance, double totalEmployeeContribution, double facWage, double expectedInterestRate, double expectedInflationRate, int yearsInvesting, int yearsRetired, int compoundsPerYear) {
        //boolean verbose = true;  //set to true to print out all the system.out.println statements in this function
        //convert interest and inflation rates to decimal
        expectedInterestRate =  expectedInterestRate /100;
        expectedInflationRate = expectedInflationRate /100;


        //calculate the real rate of return (after inflation)
        double realRateOfReturn = ((1+expectedInterestRate)/ (1+expectedInflationRate) - 1);
        /**
         // equation being modified for our use is PV = R[1-(1-i)^-n]/i
         //FV = Future Value = amount at end of investment
         //PV = presentValue  = amount you want to have at the end of the investment (in todays dollars)
         //r = rate of interest (percentage/100)
         //n = number of compounding periods per year
         //t = number of years investing
         //i = interest rate (percentage/100)
         FV = PV (1 + r / n)nt
         PV = FV / (1 + r / n)nt
         PV = futureValue / (1 + realRateOfReturn / compoundsPerYear)^(yearsInvesting * compoundsPerYear)
         presentValueOfMonthlyPayments = FutureValue / Math.pow((1 + realRateOfReturn / compoundsPerYear),(yearsInvesting * compoundsPerYear));

         */


        double presentValueOfRetirementInput = presentValue((sumDesiredAtRetirement-initialBalance-totalEmployeeContribution), realRateOfReturn, yearsInvesting);
        double percentOfEmployeeHiredPayroll = (presentValueOfRetirementInput/yearsInvesting)/( (50000) + (presentValueOfRetirementInput/yearsInvesting) + 16934 ) *100;
        double percentOfEmployeeAvgPayroll = (presentValueOfRetirementInput/yearsInvesting)/( (81000) + (presentValueOfRetirementInput/yearsInvesting) + 16934 ) *100;
        double percentOfEmployeeEndingPayroll = (presentValueOfRetirementInput/yearsInvesting)/( (facWage) + (presentValueOfRetirementInput/yearsInvesting) + 16934 ) *100;
        if (verbose) {
            System.out.printf("Annual input to retirement system required to fund (per year) for this employee assuming %.2f percent expected interest rate ", (expectedInterestRate*100) );
            System.out.print("and "+expectedInflationRate*100+"% expected inflation: $" + (int) (presentValueOfRetirementInput/yearsInvesting) +". ");
            //System.out.print("\n(A retirement burden which ranges from approx: "+ (int) percentOfEmployeeHiredPayroll+"% at hire to "+ (int) percentOfEmployeeEndingPayroll+"% at retire of their overall total cost to " +
                   // "employ where that cost includes: FAC (ranging from ~50000 to "+(int)facWage+"), this annual city cost to fund retirement, and city health insurance and other costs of $16934.");
        }
        for (int j = 0; j < yearsRetired; j++){
         //test

        }
        return presentValueOfRetirementInput;

    }
    public static double presentValue(double futureValueR, double interestRateR, double yearsR) {
        //System.out.println( " future: "+futureValueR +"  rate: "+  interestRateR +"  years: "+  yearsR); //Tests the arguments
        return futureValueR / Math.pow((1 + interestRateR/100), yearsR); // Will return the present Value
    }
    public static double futureValue(double presentValueR, double interestRateR, double yearsR) {
        //System.out.println( " present: "+presentValueR +"  rate: "+  interestRateR +"  years: "+  yearsR); //Tests the arguments
        return presentValueR * Math.pow((1 + interestRateR/100), yearsR); // Will return the future Value


    }

}


