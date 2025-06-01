import java.text.NumberFormat;
import java.util.Scanner;

public class PensionCalculatorDisbursements {
    //public final static double RATE_OF_RETURN = 0.0725;


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double LOCAL_TEST_RATE_OF_RETURN = 7.25;

        System.out.println(" ");
        System.out.println("Enter values to calculate custom retirement with (just for testing the retirementsystem.properties file)  please use main file to run a retirement calc.");
        System.out.println(" ");
        System.out.println("First, it will also output the current retirement system using wages entered  2.5% of base wage multiplier locked, 10/10/5 COLA)retire at age 55:");
        System.out.println("Secondly, it will also output a custom calculation retirement system which uses all your numbers.");
        System.out.println(" ");


        System.out.println("Enter an Base Wage at retirement. Average of fire capt 85403 and police cmd lt 88785 is approx (87000)");
        double bWage = sc.nextDouble();
        System.out.println("The top 6 non-admin salaries for police and fire average to approx $100,000 but in that is include some bonuses like sicktime buyouts not currently part of FAC");
        System.out.println("Enter an F.A.C. at retirement (Base Wage plus all bonuses and OT.. your annual buying power) at retirement (97000)");
        double fWage = sc.nextDouble();
        System.out.println("Enter an annual retirement Multiplier percent value (2.5)");
        double annualMulti = sc.nextDouble();
        boolean useFAC = false;
        //System.out.println("Is multiplier to be based on FAC for the 3rd custom calculation? Enter true to calculate using FAC, false to use basepay.  (true)");
        //boolean useFAC = sc.nextBoolean();
        System.out.println("Is multiplier to be based on FAC for the 3rd custom calculation? Enter 1 to calculate using FAC, 0 to use basepay.  (1)");
        int multiplierIsFACWageInput = sc.nextInt();
        if (multiplierIsFACWageInput == 0) {
             useFAC = false;
        } else {
             useFAC = true;
        }
        System.out.println("Does Cola compound?  (use 1 for compounding, 0 for non-compounding) (1)");
        int isColaCompounding = sc.nextInt();
        boolean isThisColaCompoundingFlag = true;
        if (isColaCompounding == 0) {
            isThisColaCompoundingFlag = false;
        }

        System.out.println("How many COLA adjustments?  (use 0 if you want no COLA's in third custom calc)  (2) ");
        int numColas = sc.nextInt();
        int colaSpacing = 0;
        double colaPercent = 0;
        //System.out.println(colaNumber);
        if (numColas !=0)
        {
            System.out.println("How many years apart are COLA adjustments for third custom calc? (5) ");
            colaSpacing = sc.nextInt();
            //System.out.println(colaSpacing);
            System.out.println("what percent are the COLA adjustments for third custom calc? (10)");
            colaPercent = sc.nextDouble();
            colaPercent = colaPercent/100; //convert to decimal for calculations
        }
        //System.out.println(colaPercent);
        System.out.println("Enter a projected future inflation rate (average over last 100 years 2.92, since 1950 years 3.55, recent 10 years 2.63) (2.63)");
        double inflateRate = sc.nextFloat();
        //inflateRate=inflateRate/100;  //convert to decimal for calculations
        double realRateOfReturn = (1+ LOCAL_TEST_RATE_OF_RETURN)/(1+inflateRate)-1; //real rate of return is calculated minus inflation rate
        System.out.println("Enter a fictional new-hire age (35)");
        int currentAge = sc.nextInt();
        System.out.println("Enter a retirement age (55 to 60 for current system) (55)");
        int retAge = sc.nextInt();
        System.out.println("Enter Years of Service: (25)");
        int yos = sc.nextInt();
        System.out.println("How many more years than an average life expectancy of 73 male retiree and 79 female spouse would you like to calculate for? (0) ");
        int deltaExtraLife = sc.nextInt();
        System.out.println("How many years older than you is your spouse? (for two years younger: -2) ");
        int deltaExtraAgeSpouse = sc.nextInt();
        sc.close();

        System.out.println("unchanged pension 2.5% of base wage retire at age 55, no SSI: ");
        calculateDisbursements(true, bWage, fWage, 2.5, true, isThisColaCompoundingFlag, 3,5, 10, inflateRate, retAge, yos, deltaExtraLife, deltaExtraAgeSpouse, currentAge);


        System.out.println("custom Calc using all data input: ");

        calculateDisbursements(true, bWage, fWage, annualMulti, useFAC, isThisColaCompoundingFlag, numColas, colaSpacing, colaPercent, inflateRate, retAge, yos, deltaExtraLife, deltaExtraAgeSpouse, currentAge);



    }

    public static double calculateDisbursements(boolean verbose, double baseWage, double facWage, double annualMultiplier, boolean useFacWage, boolean isColaCompoundingFlag, int numberColas, int colaSpace, double colaPerc, double inflateRate, int retAge, int totalYearsService, int lifeExpDiff, int spouseAgeDiff, int currentAge){

        //boolean verbose = true; //default to printing out the system.out.println statements... can turn off with false.
        double bWage =  baseWage; // base wage of the pensioner
        double fWage = facWage; // fac wage of the pensioner
        double annualMulti = annualMultiplier; // annual multiplier input
        annualMulti = annualMulti / 100; // convert to decimal for calculations
        boolean useFAC = useFacWage; // use fac wage or not
        int numColas = numberColas; // number of colas
        int colaSpacing = colaSpace; // number of years between colas
        double colaPercent = colaPerc; // cola percent
        colaPercent = colaPercent / 100; // convert to decimal for calculations
        inflateRate=inflateRate/100; //convert to decimal for calculations
        int retirementAge = retAge; // retirement age of the pensioner
        int lifeExpDelta = lifeExpDiff; // life expectancy increase or decrease from average of the pensioner (5 to live 5 years longer than avg life expectancy)
        int spouseDeltaAge = spouseAgeDiff; // age difference between pensioner and spouse  (-2 for 2 years younger)
        int yearsService = totalYearsService; // number of years worked
        int employeeCurrentAge = currentAge; // current age of the pensioner

        final double SURVIVOR_BENEFIT = 0.6; // 60% survivor benefit
        final int LIFE_EXPECTANCY = 73; // average life expectancy of male
        final int LIFE_EXPECTANCY_SPOUSE = LIFE_EXPECTANCY+6; //average additional life expectancy of a female spouse.

        // Calculate the number of years the pensioner will receive their pension
        int yearsReceivingPension = LIFE_EXPECTANCY + lifeExpDelta - retirementAge;

        // Calculate the number of years the spouse will receive their pension
        int yearsReceivingSpousePension = LIFE_EXPECTANCY_SPOUSE - LIFE_EXPECTANCY - spouseDeltaAge;
        if (yearsReceivingSpousePension < 0) {
            yearsReceivingSpousePension = 0;
        }


//custom cola pension
// Calculate the annual base wage or FAC, Cola or no cola pension amount at retirement
        double annualCustomColaPension = 0;
        double earningsBasedOn = 0;
        if (useFAC )
        {
            //it is on FAC
            earningsBasedOn = fWage;
            annualCustomColaPension = fWage * annualMulti * yearsService;

        }
        else
        {
            //it is on base wage
            earningsBasedOn = bWage;
            annualCustomColaPension = bWage * annualMulti * yearsService;
        }

// Calculate the annual buying power of the pension at the time of retirement
        double initialCustomColaPension = annualCustomColaPension;
        //calculate a non-compounding cola amount to be added at cola time for non-compounding colas
        double straightCola = initialCustomColaPension * colaPercent;
        String colaType = "Compounding";
        if (!isColaCompoundingFlag) {
            colaType = "Non-Compounding";
        }

//track total payout
        double totalPayoutCustomCola = 0;

        int colaCustomCounter = 0;
        double lastPensionersCustomColaPension = initialCustomColaPension;

        for(int i = 1; i <= yearsReceivingPension; i++)
        {
            if ((numColas !=0) && (i % colaSpacing == 0) && (colaCustomCounter < numColas)) {
                //if it is every cola year and there haven't been too many cola years, add a cola and increment the counter
                colaCustomCounter++;
                if (isColaCompoundingFlag) {
                    //if it is compounding, add the cola to the last year's pension
                    lastPensionersCustomColaPension += lastPensionersCustomColaPension * colaPercent;
                }
                else {
                    //if it is not compounding, add the straight cola to the last year's pension
                    lastPensionersCustomColaPension += straightCola;
                }
                //lastPensionersCustomColaPension += lastPensionersCustomColaPension * colaPercent;
            }
            //otherwise inflate
            lastPensionersCustomColaPension -= lastPensionersCustomColaPension * inflateRate;
            //add payment to total
            totalPayoutCustomCola += lastPensionersCustomColaPension;
        }

// Calculate the annual pension for the surviving spouse
        double annualSpouseCustomColaPension = lastPensionersCustomColaPension * SURVIVOR_BENEFIT;

// Calculate the annual buying power of the spouse's pension at the time of retirement
        double buyingPowerSpouseCustomColaPension = annualSpouseCustomColaPension;
        for(int i = 1; i <= yearsReceivingSpousePension; i++){
            buyingPowerSpouseCustomColaPension -= buyingPowerSpouseCustomColaPension * inflateRate;
            //add payment to total
            totalPayoutCustomCola += buyingPowerSpouseCustomColaPension;
        }
        //return this totalPayoutCustomCola later;

if (verbose) {
    NumberFormat nf= NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);


    System.out.println("\nAssumptions: " + nf.format(annualMulti * 100 )+ "% multiplier based on $" + (int) earningsBasedOn + " (out of $"+(int)fWage+" FAC) with " + colaCustomCounter + " "+colaType+" C.O.L.A.'s of " + nf.format(colaPercent * 100 )+ "% spaced " + colaSpacing + " years apart, future Inflation "+ nf.format(inflateRate *100)+"%, No SSI or previous/other 401/457 disbursements included. ");
    System.out.println("The initial retirement is: $" + (int) initialCustomColaPension + ", Age: " + retirementAge + "yrs old and " + yearsService + " yrs of service, with life expectancy of " + LIFE_EXPECTANCY + " + " + lifeExpDelta + " and life expectancy of spouse as " + LIFE_EXPECTANCY_SPOUSE + " + " + lifeExpDelta + " and spouse's age diff being " + spouseAgeDiff + ".");
    System.out.println(" ");

    System.out.println("Annual buying power in today's dollars at retirement is $" + (int) initialCustomColaPension + " (" + (int) (initialCustomColaPension / fWage * 100) + "% of working buying power.)");
    System.out.println("This buying power is reduced by inflation at rate of " + (inflateRate * 100) + "% per year which COLA adjustments partially offset, leaving a ");
    System.out.print("Buying power at employee's end of life of: $");
    System.out.println((int) lastPensionersCustomColaPension + " (" + (int) (lastPensionersCustomColaPension / fWage * 100) + "% of working buying power.) ");
    //System.out.println("\nWe consider a dignified retirement to be 60% of worker buying power reducing to no less than 40% over time and never below the federal poverty level.");
    System.out.println(" ");
    System.out.println("The Federal Poverty Level for a Family of 2: $18,300.  Percent of pensioner to poverty level:" + (int) (lastPensionersCustomColaPension / 18300 * 100) + "%.");
    //System.out.println(" ");
    if (yearsReceivingSpousePension > 0) {
        System.out.println("Annual buying power for widow(er) is $"+ (int) (lastPensionersCustomColaPension * .6) + " in today's dollars and reducing to $"+(int) buyingPowerSpouseCustomColaPension + " over " + yearsReceivingSpousePension + " years until widow(er)'s end of life.");
        System.out.println("Federal Poverty Level for Single Person: $13,500.  Percent of widow to poverty level:" + (int) (buyingPowerSpouseCustomColaPension / 13500 * 100) + "%.");
    }
    System.out.println(" ");
    //System.out.println("We DON'T have social security. But it is interesting to compare SSI payments starting at age 62 (at a 70% penalty) (and measured in todays buying power) would approximately ");
    //System.out.println("be  $" + (int) (fWage * 1.5 / 100 * 30 * .7) + " which keeps most other retirees in many other fire departments and civilian jobs at least at " + (int) (fWage * 1.5 / 100 * 30 * .7 / 18300 * 100) + "% of poverty level with an annually cost of living adjustment ");
    //System.out.println("by congress (without even including any other savings, 401k, or retirement plans.)  A Windfall Elimination Provision (WEP) would reduce this amount by 40% for someone who didnt have ");
    //System.out.println("employment prior to hiring and worked 25 years, but if they worked 5 years elsewhere the total of 30 years under SSI makes the WEP only reduce by 10-15%");
    //System.out.println(" ");

    System.out.printf("Total Payout to retiree's family in today's dollars of this calculation $" + (int) totalPayoutCustomCola + ".");
    System.out.println(" ");
}
else {
    NumberFormat nf= NumberFormat.getInstance();
    nf.setMaximumFractionDigits(2);


    //System.out.println("\nRetirement Calculation with assumptions " + nf.format(annualMulti * 100 )+ "% multiplier based on $" + (int) earningsBasedOn + " out of $"+(int)fWage+" FAC with " + numColas + " compounding C.O.L.A.'s of " + nf.format(colaPercent * 100 )+ "% spaced " + colaSpacing + " years apart, future Inflation "+ nf.format(inflateRate *100)+"%, No SSI ");
    //System.out.println("The initial retirement is: $" + (int) initialCustomColaPension + ", Age: " + retirementAge + "yrs old and " + yearsService + " yrs of service, with life expectancy of " + LIFE_EXPECTANCY + " + " + lifeExpDelta + " and life expectancy of spouse as " + LIFE_EXPECTANCY_SPOUSE + " + " + lifeExpDelta + " and spouse's age diff being " + spouseAgeDiff + ".");
    //System.out.println(" ");

    System.out.println("Annual buying power in today's dollars at retirement is $" + (int) initialCustomColaPension + " (" + (int) (initialCustomColaPension / fWage * 100) + "% of working buying power.)");
    System.out.println("This buying power is reduced by inflation at rate of " + (inflateRate * 100) + "% per year which COLA adjustments partially offset, leaving a ");
    System.out.print("Buying power at employee's end of life of: $");
    System.out.println((int) lastPensionersCustomColaPension + " (" + (int) (lastPensionersCustomColaPension / fWage * 100) + "% of working buying power.) ");
    //System.out.println("\nWe consider a dignified retirement to be 60% of worker buying power reducing to no less than 40% over time and never below the federal poverty level.");
    //System.out.println(" ");
    //System.out.println("The Federal Poverty Level for a Family of 2: $18,300.  Percent of pensioner to poverty level:" + (int) (lastPensionersCustomColaPension / 18300 * 100) + "%.");
    //System.out.println(" ");
    if (yearsReceivingSpousePension > 0) {
        System.out.println("Annual buying power for widow(er) is $"+ (int) (lastPensionersCustomColaPension * .6) + " in today's dollars and reducing to $"+(int) buyingPowerSpouseCustomColaPension + " over " + yearsReceivingSpousePension + " years until widow(er)'s end of life.");
        //System.out.println("Federal Poverty Level for Single Person: $13,500.  Percent of widow to poverty level:" + (int) (buyingPowerSpouseCustomColaPension / 13500 * 100) + "%.");
    }
    //System.out.println(" ");
    //System.out.println("We DON'T have social security. But it is interesting to compare SSI payments starting at age 62 (at a 70% penalty) (and measured in todays buying power) would approximately ");
    //System.out.println("be  $" + (int) (fWage * 1.5 / 100 * 30 * .7) + " which keeps most other retirees in many other fire departments and civilian jobs at least at " + (int) (fWage * 1.5 / 100 * 30 * .7 / 18300 * 100) + "% of poverty level with an annually cost of living adjustment ");
    //System.out.println("by congress (without even including any other savings, 401k, or retirement plans.)  A Windfall Elimination Provision (WEP) would reduce this amount by 40% for someone who didnt have ");
    //System.out.println("employment prior to hiring and worked 25 years, but if they worked 5 years elsewhere the total of 30 years under SSI makes the WEP only reduce by 10-15%");
    //System.out.println(" ");

    //System.out.printf("Total Payout to retiree's family in today's dollars of this calculation $" + (int) totalPayoutCustomCola + ".");
    //System.out.println(" ");

}

    return totalPayoutCustomCola;


    }


}

