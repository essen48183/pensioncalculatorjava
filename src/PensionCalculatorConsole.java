//
// http://levelupcontractingllc.com/levelupcontractingllc.com/softwaredeployment/deployments/pensioncalculator/out/artifacts/pensioncalculator_jar/PensionCalculatorConsole-1.0.exe
// jpackage --type exe --input . --dest . --main-jar pensioncalculator_jar.jar --main-class PensionCalculatorConsole --win-console --win-shortcut --win-menu --win-dir-chooser --app-version "%appver%" --name "Pension Calculator" --verbose
// set appver=%date:~6%%time:~7%
//
// jpackage --type exe --input . --dest ../.. --main-jar pensioncalculator_jar.jar --main-class PensionCalculatorConsole --win-console --win-shortcut --win-menu --win-dir-chooser --app-version "1.2.13" --name "Pension Calculator" --verbose

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Scanner;
import java.io.*;


public class PensionCalculatorConsole  {

    static Properties prop;

    private static void loadProperties() {
        prop = new Properties();
        InputStream in = PensionCalculatorConsole.class
                .getResourceAsStream("retirementsystem.properties");
        try {
            //System.out.println("Loading properties file");
            prop.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) { //throws Exception {

        boolean isOutputVerbose = false;

        final int LIFE_EXPECTANCY = 73; // average life expectancy of male
        //final int LIFE_EXPECTANCY_DELTA_SPOUSE = 6; //average additional life expectancy of a female spouse.
        final int currentYear = LocalDateTime.now().getYear();

        Scanner sc = new Scanner(System.in);

        System.out.println(" ");
        System.out.println("Enter values to calculate a sample custom retirement with.  A total system cost will also output using current employee and spouse age and hire date data. ");
        System.out.println(" ");

        String line = "";
        int inputBaseWage = 87000;
        System.out.print("\nEnter a (today's dollar) BASE WAGE at retirement. Average of FD Capt. $85403 and PD Cmd. Lt. $88785 is approx (87000) : ");
        line = sc.nextLine();
        if (line != "") {
            inputBaseWage = Integer.parseInt(line);
        } else {
            System.out.print("87000 ");
        }

        System.out.println("\nThe top 6 non-admin salaries for police and fire average to approx 100,000 but include some bonuses like sicktime buyouts not part of FAC");
        int inputFacWage = 97000;
        System.out.print("Enter a (today's dollar) F.A.C. WAGE at retirement (Base Wage plus all bonuses and OT.. your annual buying power) at retirement. (97000) : ");
        line = sc.nextLine();
        if (line != "") {
            inputFacWage = Integer.parseInt(line);
        } else {
            System.out.print("97000");
        }

        double inputMultiplier = 2.5;
        System.out.print("\nEnter an annual retirement multiplier percent value (2.5) : ");
        line = sc.nextLine();
        if (line != "") {
            inputMultiplier = Double.parseDouble(line);
        } else {
            System.out.print("2.5");
        }
        //inputMultiplier=inputMultiplier/100;

        int inputMultiplierIsFACWage = 1;
        System.out.print("\nIs multiplier to be based on FAC for the custom calculation? Enter 1 to calculate using FAC, 0 to use basepay. (1) : ");
        line = sc.nextLine();
        if (line != "") {
            inputMultiplierIsFACWage = Integer.parseInt(line);
        } else {
            System.out.print("1 ");
        }
        //set boolean multiplierIsFACWage flag
        boolean inputMultiplierIsFACWageFlag = false;
        if (inputMultiplierIsFACWage != 0) {
            inputMultiplierIsFACWageFlag = true;
        }

        int colaNumber = 3;
        int colaSpacing = 5;
        double colaPercent = 8;
        int isColaCompounding = 1;
        boolean isColaCompoundingFlag = true;

        System.out.println("\nPrevious system had 10%/10%/5% non-compounding COLAs spaced 5 years apart. This is approximately equal to entering 3 compounding COLAS spaced 5 years at 8%. ");

        System.out.print("\nCompound vs Straight COLA: is the COLA compounding or is it a straight percent converted to an unchanging cash value at time of retirement?  " +
                "\n(It is easier to track, calculate, and administer a system with a lower but equivalent compounding COLA) " +
                "\nI the COLA compounding? (use 1 for compounding, 0 for straight one time calculated to dollar amount) (1): ");
        line = sc.nextLine();
        if (line != "") {
            isColaCompounding = Integer.parseInt(line);
        } else {
            System.out.print("1 ");
        }
        if (isColaCompounding != 0)
        {
            isColaCompoundingFlag = true;
        }
        else {
            isColaCompoundingFlag = false;
        }

        System.out.print("How many COLA adjustments?  (use 40 or more if you intend annual colas and use 0 if you want no COLA's in calculation... they stop at life expectancy)  (3) : ");
        line = sc.nextLine();
        if (line != "") {
            colaNumber = Integer.parseInt(line);
        } else {
            System.out.print("3 ");
        }
        if (colaNumber != 0) {
            colaSpacing = 1;
            System.out.print("\nHow many years apart are the COLA adjustments? (5) : ");
            line = sc.nextLine();
            if (line != "") {
                colaSpacing = Integer.parseInt(line);
            } else {
                System.out.print("5 ");
            }
            //colaPercent = 8;
            System.out.print("\nWhat percent are the COLA adjustments? (8) : ");
            line = sc.nextLine();
            if (line != "") {
                colaPercent = Double.parseDouble(line);
            } else {
                System.out.print("8 ");
            }
            //colaPercent = colaPercent/100;
        }
        double expectedFutureInflationRate = 2.63;
        System.out.print("\n(When deciding whether to use a projected inflation rate (2.63-3.55 range) or projected wage growth rate (2-3.5 range). Keep in mind that if you opt to use a wage growth rate which is less than  " +
                "\nwhat you expect the amount of inflation to be, this number is used to calculate present values... the nexus point is the date of retirement for the individuals, so the total amount needed in present value " +
                "\nto be 100 percent funded will be lower and the degradation of buying power in retirement as you approach end of life will not be as accurate showing higher than " +
                "expected buying power.)" +
                "\n\nEnter a projected future inflation rate or projected wage growth rate (average inflation over last 100 years 2.92, since 1950 years 3.55, recent 10 years 2.63) (2.63) :");
        line = sc.nextLine();
        if (line != "") {
            expectedFutureInflationRate = Double.parseDouble(line);
        } else {
            System.out.print("2.63 ");
        }
        //expectedFutureInflationRate = expectedFutureInflationRate/100;
        int inputRetAge = 60;
        System.out.print("\nEnter a retirement age that is not based on years of service. (60 for current system) (60) :");
        line = sc.nextLine();
        if (line != "") {
            inputRetAge = Integer.parseInt(line);
        } else {
            System.out.print("60 ");
        }
        int inputCareerYearsService = 25;
        System.out.print("\nEnter an alternative number of years of service needed to retire before the default retirement age: (25) : ");
        line = sc.nextLine();
        if (line != "") {
            inputCareerYearsService = Integer.parseInt(line);
        } else {
            System.out.print("25 ");
        }
        int inputMinAgeForYearsService = 55;
        System.out.print("\nEnter minimum age you must be to retire if using years of service (use same as default retirement age to not allow years of service): (55)");
        line = sc.nextLine();
        if (line != "") {
            inputMinAgeForYearsService = Integer.parseInt(line);
        } else {
            System.out.print("55 ");
        }

        //int inputAgeHired = inputRetAge - inputCareerYearsService;
        int deltaExtraLife = 0;
        System.out.print("\nHow many more years than an average life expectancy of 73 male retiree and 79 female spouse would you like to calculate for? (enter 6 if female employee) (0) : ");
        line = sc.nextLine();
        if (line != "") {
            deltaExtraLife = Integer.parseInt(line);
        } else {
            System.out.print("0 ");
        }
        System.out.println("\nNEXT INPUTS ARE FOR FICTIONAL NEW HIRE ONLY USEFUL FOR CALCULATING THINGS LIKE THE BUYING OF TIME YOU CAN JUST HIT ENTER TO USE DEFAULTS IF INTERESTED IN SYTSTEM COSTS ONLY");

        int inputAgeHired = 30;
        System.out.print("\nEnter a Fictional New Hire Age (30) : ");
        line = sc.nextLine();
        if (line != "") {
            inputAgeHired = Integer.parseInt(line);
        } else {
            System.out.print("30 ");
        }
        //fake comment to allow a commit

        int inputSpouseAgeDiff = -2;
        System.out.print("\nWhat is the age difference of fictional spouse?  ( 2 years younger would be -2) (for female employee or male with no widow: enter -6) (-2) : ");
        line = sc.nextLine();
        if (line != "") {
            inputSpouseAgeDiff = Integer.parseInt(line);
        } else {
            System.out.print("-1 ");
        }
        int inputWantVerboseOutput = 0;

        /**System.out.print("\nDo you want a long verbose output including all employees and total system costs? (1 for yes, 0 for no) (0) : ");
        line = sc.nextLine();
        if (line != "") {
            inputWantVerboseOutput = Integer.parseInt(line);
        } else {
            System.out.print("0 ");
        }
        */

        //set boolean verbose flag
        if (inputWantVerboseOutput != 0) {
            isOutputVerbose = true;
        }


        PensionCalculatorDisbursements disbursements = new PensionCalculatorDisbursements();
        PensionCalculatorPaymentsInto paymentsInto = new PensionCalculatorPaymentsInto();

//beginning of employee data file read in section

        System.out.println("\n\nAggregate Data came from the following employee hire date and age history:\n");
        //initialize all running totals to 0
        double totalDisbursementsFromCsvAgeFile = 0;
        double totalPaymentsIntoFromCsvAgeFile = 0;
        //double totalInsuranceCostFromCsvAgeFile = 0;//total city paid while working insurance cost.. used to calculate the percent of employee cost that is retirmement cost
        double employeeApproxContribution = 0;
        double totalAllEmployeeContributions = 0;

        //double UnchangedTotalDisbursementsFromCsvAgeFile = 0;
        //double UnchangedTotalPaymentsIntoFromCsvAgeFile = 0;

        int numberOfYearsToDivideTotalPaymentsBy = 0; //total number of years of each employee's career all summed up

        //setup variables that will each be modified per employee in from file
        //int employeeOldSystemEarliestEligibleRetirementAge = 55;
        //int fileEmployeeID = 0;
        int fileEmployeeHiredYear = 0;
        int fileEmployeeBornYear = 0;
        int fileEmployeeSpouseBornYear = 0;
        int fileEmployeeCurrentAge = 0;
        int fileEmployeeSpouseCurrentAge = 0;
        int fileSpouseAgeDiff = 0;
        int fileEmployeeHiredAge = 0;
        //int fileTotalNumYearsNeededForRetByAge = 0;
        //int fileTotalNumbYearsNeededToRetireByYearsService = 0;
        int fileTotalNumYearsNeededToRetire = 0;
        //values from 2022 city budget
        double cityAnnualWageAndBonusPayments = 4949000;
        double cityAnnualInsurancePayments = 1033000;


        //read in the retirementsystem.properties file
        loadProperties();
        double eachEmployeeInsuranceAnnualCostToCity = Double.parseDouble(prop.getProperty("eachEmployeeInsuranceAnnualCostToCity"));//get the insurance cost from the properties file
        double totalCityInsuranceCostWhileWorking = 0; //initialize aggregate working insurance rate to track percent of total cost that is retirement cost
        int totalNumberEmployees = Integer.parseInt(prop.getProperty("totalNumberEmployees"));
        System.out.println("Employee data used from " + totalNumberEmployees + " current employees for this calculation: ");
        double expectedSystemFutureRateReturn = Double.parseDouble(prop.getProperty("expectedSystemFutureRateReturn"));
        System.out.println("Expected future rate of return " + expectedSystemFutureRateReturn + "%. ");
        //System.out.println(prop.toString());

        for (int empCounter = 1; (empCounter <= Integer.parseInt(prop.getProperty("totalNumberEmployees"))); empCounter++) {
            System.out.println("\n\n______________________________________________________________");
            //System.out.println("\n______________________________________________________________");
            System.out.print("\nEmployee: " + empCounter + ", ");
            System.out.print("Name: " + prop.getProperty(empCounter + ".name") + ", ");
            //fileEmployeeID = Integer.parseInt(prop.getProperty(empCounter + ".name"));
            System.out.print("Hired: " + prop.getProperty(empCounter + ".hired") + ", ");
            fileEmployeeHiredYear = Integer.parseInt(prop.getProperty(empCounter + ".hired"));
            System.out.print("At Age: " + (Integer.parseInt(prop.getProperty(empCounter + ".hired")) - Integer.parseInt(prop.getProperty(empCounter + ".dob")))+ ", ");
            //fileEmployeeHiredYear =
            System.out.print("DOB: " + prop.getProperty(empCounter + ".dob") + ", ");
            fileEmployeeBornYear = Integer.parseInt(prop.getProperty(empCounter + ".dob"));
            System.out.print("Spouse DOB: " + prop.getProperty(empCounter + ".spousedob") + " ");
            fileEmployeeSpouseBornYear = Integer.parseInt(prop.getProperty(empCounter + ".spousedob"));
            System.out.println(" ");

            //derive some data
            fileEmployeeCurrentAge = LocalDateTime.now().getYear() - fileEmployeeBornYear;
            fileEmployeeSpouseCurrentAge = LocalDateTime.now().getYear() - fileEmployeeSpouseBornYear;
            fileSpouseAgeDiff = fileEmployeeSpouseCurrentAge - fileEmployeeCurrentAge;
            fileEmployeeHiredAge = fileEmployeeHiredYear - fileEmployeeBornYear;
            //fileTotalNumYearsNeededForRetByAge = inputRetAge - fileEmployeeHiredAge;
           // fileTotalNumbYearsNeededToRetireByYearsService = inputCareerYearsService;

            //figure out how many years needed to retire of file import values

            if (inputRetAge <= (inputCareerYearsService + fileEmployeeHiredAge)) { //if the default retirement age is less than  using years of service, use default age only
                fileTotalNumYearsNeededToRetire = inputRetAge - fileEmployeeHiredAge;
                System.out.println("Eligible to retire in "+(int)(fileEmployeeHiredYear+fileTotalNumYearsNeededToRetire)+" with "+fileTotalNumYearsNeededToRetire+" years service at the default retirement age of " + inputRetAge) ;
            } else if ((fileEmployeeHiredAge + inputCareerYearsService) < inputMinAgeForYearsService){ //if you will be younger than min ret age using years of service, use youngest ret age.
                fileTotalNumYearsNeededToRetire = inputMinAgeForYearsService - fileEmployeeHiredAge;
                System.out.println("Eligible to retire in "+(int)(fileEmployeeHiredYear+fileTotalNumYearsNeededToRetire)+" with "+fileTotalNumYearsNeededToRetire+" years service at minimum Age ("+ inputMinAgeForYearsService+")to use years of service ");
            } else  {//just use years of service
                fileTotalNumYearsNeededToRetire = inputCareerYearsService;
                System.out.println("Eligible to retire in "+(int)(fileEmployeeHiredYear+fileTotalNumYearsNeededToRetire)+" at age " + (fileTotalNumYearsNeededToRetire + inputAgeHired) + " based on "+fileTotalNumYearsNeededToRetire+" years of service");
            }
            if (fileTotalNumYearsNeededToRetire < 1) { //you need at least 10 years to retire
                fileTotalNumYearsNeededToRetire = 1;
                System.out.println("Oops, they need at least 1 years to retire... adjusting retirement age to "+(inputAgeHired + fileTotalNumYearsNeededToRetire)+".");
            }


            double eachEmployeeAverageWageAndBonusPayments = cityAnnualWageAndBonusPayments/totalNumberEmployees;
            employeeApproxContribution = (inputFacWage * .06 * fileTotalNumYearsNeededToRetire);
            totalAllEmployeeContributions = totalAllEmployeeContributions + employeeApproxContribution;

            //double unchangedEmployeeTotalDisbursementsFromCsvAgeFile = disbursements.calculateDisbursements(isOutputVerbose, inputBaseWage, inputFacWage, 2.5, true, 2, 5, 10, expectedFutureInflationRate, (fileTotalNumYearsNeededToRetire + fileEmployeeHiredAge), fileTotalNumYearsNeededToRetire, 0, fileSpouseAgeDiff, fileEmployeeCurrentAge);
            //double presentValueUnchangedPlanEmployeeFromCsvAgeFile = paymentsInto.calculateDiscountPayment(isOutputVerbose, unchangedEmployeeTotalDisbursementsFromCsvAgeFile, 0, inputFacWage * .06 * fileTotalNumYearsNeededToRetire, inputFacWage, expectedSystemFutureRateReturn, expectedFutureInflationRate, fileTotalNumYearsNeededToRetire, LIFE_EXPECTANCY - employeeOldSystemEarliestEligibleRetirementAge, 1);
            //double totalCityPaysForUnchangedPlanEmployeeFromCsvAgeFile = presentValueUnchangedPlanEmployeeFromCsvAgeFile;
            //System.out.println("Adding up cost for total City input (pre investment gains, in today's dollars) over a career (as currently actuarial calculated 2.5/10/10/5 cola retire Age 55) pension: $" + (int) totalCityPaysForUnchangedPlanEmployeeFromCsvAgeFile);
            int employeeEarliestEligibleRetirementAge = ((fileEmployeeHiredYear + fileTotalNumYearsNeededToRetire) - currentYear + fileEmployeeCurrentAge);
            double employeeChangedTotalDisbursementsFromCsvAgeFile = disbursements.calculateDisbursements(isOutputVerbose, inputBaseWage, inputFacWage, inputMultiplier, inputMultiplierIsFACWageFlag, isColaCompoundingFlag, colaNumber, colaSpacing, colaPercent, expectedFutureInflationRate, employeeEarliestEligibleRetirementAge, fileTotalNumYearsNeededToRetire, 0, fileSpouseAgeDiff, fileEmployeeCurrentAge);
            double presentValueEmployeeChangedFromCsvAgeFile = paymentsInto.calculateDiscountPayment(isOutputVerbose, employeeChangedTotalDisbursementsFromCsvAgeFile, 0, employeeApproxContribution, inputFacWage, expectedSystemFutureRateReturn, expectedFutureInflationRate, fileTotalNumYearsNeededToRetire, LIFE_EXPECTANCY - employeeEarliestEligibleRetirementAge, 1);
            double employeeChangedTotalPaymentsIntoFromCsvAgeFile = presentValueEmployeeChangedFromCsvAgeFile;

            totalDisbursementsFromCsvAgeFile = totalDisbursementsFromCsvAgeFile + employeeChangedTotalDisbursementsFromCsvAgeFile;
            totalPaymentsIntoFromCsvAgeFile = totalPaymentsIntoFromCsvAgeFile + employeeChangedTotalPaymentsIntoFromCsvAgeFile;
            totalCityInsuranceCostWhileWorking = totalCityInsuranceCostWhileWorking + eachEmployeeInsuranceAnnualCostToCity;

            employeeApproxContribution = (inputFacWage * .06 * fileTotalNumYearsNeededToRetire);
            totalAllEmployeeContributions = totalAllEmployeeContributions + employeeApproxContribution;
            System.out.println("City contribution (pre-investement gains, in today's dollars)  $" + (int) employeeChangedTotalPaymentsIntoFromCsvAgeFile +
                    ". Employee contribution ~$" + (int)employeeApproxContribution + " (over " + (int) fileTotalNumYearsNeededToRetire + " years of service.)");
            System.out.println("This employee's Total benefit (post-investment gains) of a whole career (today's dollars): $" + (int) employeeChangedTotalDisbursementsFromCsvAgeFile );

            if (isOutputVerbose) {
                System.out.println("All employees added in so far, the city will have contributed by the time they all retire: $" + (int) totalPaymentsIntoFromCsvAgeFile + ". ");
                System.out.println("All employee added so far above for total benefit out of a whole career out (today's dollars): $" + (int) totalDisbursementsFromCsvAgeFile + ". ");

            }
            //UnchangedTotalDisbursementsFromCsvAgeFile = UnchangedTotalDisbursementsFromCsvAgeFile + unchangedEmployeeTotalDisbursementsFromCsvAgeFile;
            //UnchangedTotalPaymentsIntoFromCsvAgeFile = UnchangedTotalPaymentsIntoFromCsvAgeFile + totalCityPaysForUnchangedPlanEmployeeFromCsvAgeFile;

            numberOfYearsToDivideTotalPaymentsBy = numberOfYearsToDivideTotalPaymentsBy + fileTotalNumYearsNeededToRetire; //increment big total number of years to divide by

        }

        System.out.println("\n\n^_____________________________________________________________^");
        System.out.println("                   AGGREGATE DATA");
        System.out.println("______________________________________________________________");


        System.out.println("\nEmployee data used from " + totalNumberEmployees + " current employees for this calculation: ");
        System.out.println("Expected future rate of return " + expectedSystemFutureRateReturn + "%.  Expected future inflation rate " + expectedFutureInflationRate + "%. ");
        System.out.println("Multiplier " + inputMultiplier + ".  Multiplier based on F.A.C. wages? " + inputMultiplierIsFACWageFlag + ".  COLA number " + colaNumber + ".  COLA spacing " + colaSpacing + ".  COLA percent " + colaPercent + ".  ");
        System.out.println("Eligibility (employee opts to leave at earliest opportunity) with a min age of "+ inputMinAgeForYearsService+" needed to retire with a min of "+inputCareerYearsService+" years service, or alternatively 10 years vestment and a min age of "+inputRetAge+": \n");

        //System.out.println("\nTotal lifetime projected payout (post return gains, in today's dollars) of current actuarially calculated \"2.5 FAC 10/10/5 cola, age 55, 6% employee contribution\" unchanged system for all employees for a whole career: $" + (int) UnchangedTotalDisbursementsFromCsvAgeFile + ". ");
        //System.out.println("Total of how current actuarially calculated \"2.5 FAC 10/10/5 cola\" unchanged system projected payments (pre return gains) into system projected in today's dollars for all employees for career: $" + (int) UnchangedTotalPaymentsIntoFromCsvAgeFile);

        System.out.println("\nTotal lifetime aggregate benefit payout (post return gains, in today's dollars) for all employees in system file as calculated with the new inputs: $" + (int) totalDisbursementsFromCsvAgeFile + ". ");
        System.out.println("Total lifetime aggregate projected investment into by city (pre-return gains and not including employee contributions) into system for all employees: $" + (int) totalPaymentsIntoFromCsvAgeFile + ". ");
        double cityAnnualPaymentsForDisplay = (totalPaymentsIntoFromCsvAgeFile/numberOfYearsToDivideTotalPaymentsBy*totalNumberEmployees);

        double cityAnnualPercentOfPayroll = (cityAnnualPaymentsForDisplay/(cityAnnualPaymentsForDisplay+cityAnnualWageAndBonusPayments+cityAnnualInsurancePayments));
        System.out.println("\n*** Total projected payments annually by city (not including employee contributions) for entire system: $" + (int) (totalPaymentsIntoFromCsvAgeFile/numberOfYearsToDivideTotalPaymentsBy*totalNumberEmployees) +".");
        System.out.println("*** Which is ~"+ (int) (cityAnnualPercentOfPayroll*100) +"% of the total cost of payroll. (payroll which consists of these retirement contributions above of " +
                "*** \n$"+(int) cityAnnualPaymentsForDisplay+", and payroll wages and budgeted OT/bonuses of $"+(int) cityAnnualWageAndBonusPayments+" and health/life/vision/dental insurance costs of $"+(int)cityAnnualInsurancePayments+")");
        //System.out.println("\nOverall entire career-long for whole system shortfall caused by 1996 changes to restore to values input: $" + (int) (totalPaymentsIntoFromCsvAgeFile - UnchangedTotalPaymentsIntoFromCsvAgeFile) + ". (neg value indicates system surplus)");
        //System.out.println("To move from current actuarially calculated as \"2.5 FAC 10/10/5 cola ret age 55, 6% employee contribution\" to as the inputs were just entered, The 8 year smoothed (Additional to current) payments needed to be");
        //System.out.println("paid into (pre investment gain) for whole system to invoke fix: $" + (int) ((totalPaymentsIntoFromCsvAgeFile - UnchangedTotalPaymentsIntoFromCsvAgeFile) / 8) + " per year for 8 years to catch up. (neg value = surplus)");
        System.out.println("\nThe total annual projected payment above includes all current and future employees and assumes a goal of 100% funding ratio for each current employee at their MINIMUM retirement age," +
                "\n(It does not include current retirees which should but may not be 100% funded) " +
                "\nbut it does not include any current surplus/shortfall, due for example to previous over/under payments or over/under-performance of investments, from past years. " +
                "\n(the difference between this cost calculated at current benefit levels vs that surplus or shortfall is essentially fixed and unchangeable except by improved fund investment return performance or additional capital input" +
                "\nThe whole system (including prior retirees) investment gains/losses are smoothed over 5 years and any unfunded legacy costs from prior underfunding are currently smoothed in over 8 years to determine these additional annual adjustments to cost. )");
        System.out.println("\n______________________________________________________________");
        System.out.println("\n______________________________________________________________");
//end of file read in section

        //figure out how many years needed to retire of input values

        System.out.println("\n^--- Calculations above are using using the user input values for required years of service and percent assumptions given but " +
                "\n     using the ages and hire dates of the actual employees imported from retirementsystem.properties file.  ");
        System.out.println("\nv--- Below is a separate individual fictional employee (using all input values including ages and hire dates input and was not included as part of the aggregate data presented above.)");
        System.out.println("\n______________________________________________________________");
        System.out.println("\n______________________________________________________________");

        System.out.print("\n\nA fictional new hire in a system setup with a minimum of  "+inputCareerYearsService+" years of service to retire (as long as they have a minimum age of "+ inputMinAgeForYearsService+"), or alternatively 1 years vestment and an age of "+inputRetAge+", \n");
        int totalNumYearsNeededToRetire = 0;
        if ((inputAgeHired + inputCareerYearsService) < (inputMinAgeForYearsService)){ //if you will be younger than min ret age using years of service, use youngest ret age.
            totalNumYearsNeededToRetire = inputMinAgeForYearsService - inputAgeHired;
            System.out.println("retiring with "+totalNumYearsNeededToRetire+" years at the Minimum Age ("+ inputMinAgeForYearsService+") to use Years of Service ");
        } else if (inputRetAge <= (inputCareerYearsService + inputAgeHired)) { //if the default retirement age is less than  using years of service, use default age only
            totalNumYearsNeededToRetire = inputRetAge - inputAgeHired;
            System.out.println("retiring based on aging out with "+totalNumYearsNeededToRetire+" years at mandatory retirement age of " + inputRetAge +" ") ;
        }  else  {//just use years of service
            totalNumYearsNeededToRetire = inputCareerYearsService;
            System.out.println("retiring with "+totalNumYearsNeededToRetire+" years service at age " + (inputCareerYearsService + inputAgeHired) + " based on years of service ");
        }
        if (totalNumYearsNeededToRetire < 1) { //you need at least 1 year9s)to retire
                totalNumYearsNeededToRetire = 1;
                System.out.println("You need at least 1 years to retire... adjusting retirement age to "+inputAgeHired+totalNumYearsNeededToRetire+".");
        }
        System.out.println("has the following cost/benefit calculations: ");


/**
        double totalPayoutUnchangedPension = disbursements.calculateDisbursements(true, inputBaseWage, inputFacWage, 2.5, true, 2, 5, 10, expectedFutureInflationRate, 55, 25, deltaExtraLife, inputSpouseAgeDiff, inputCurrentAge);
        double presentValueEmployeeUnchangedPension = paymentsInto.calculateDiscountPayment(true, totalPayoutUnchangedPension, 0.0d, inputFacWage * .06 * 25, inputFacWage, expectedSystemFutureRateReturn, expectedFutureInflationRate, 25, LIFE_EXPECTANCY + deltaExtraLife - 55, 1);
        double totalCityPaysForEmployeeIntoUnchangedPension = presentValueEmployeeUnchangedPension;
        double annualCityPaysForEmployeeIntoUnchangedPension = totalCityPaysForEmployeeIntoUnchangedPension / 25;
        System.out.println("(Pre-investment-gain) City input (in today's dollars) over a career (hired to retired) of unchanged pension: $" + (int) totalCityPaysForEmployeeIntoUnchangedPension);
*/

        int earliestEligibleRetirementAge = (inputAgeHired + totalNumYearsNeededToRetire);
        double totalPayoutCustomCalc = disbursements.calculateDisbursements(true, inputBaseWage, inputFacWage, inputMultiplier, inputMultiplierIsFACWageFlag, isColaCompoundingFlag, colaNumber, colaSpacing, colaPercent, expectedFutureInflationRate, earliestEligibleRetirementAge, totalNumYearsNeededToRetire, deltaExtraLife, inputSpouseAgeDiff, inputAgeHired);
        double presentValueEmployeeCalculatedPension = paymentsInto.calculateDiscountPayment(true, totalPayoutCustomCalc, 0.0d, inputFacWage * .06 * 25, inputFacWage, expectedSystemFutureRateReturn, expectedFutureInflationRate, 25, LIFE_EXPECTANCY + deltaExtraLife - earliestEligibleRetirementAge, 1);
        double totalCityPaysForEmployeeIntoCalculatedPension = presentValueEmployeeCalculatedPension;
        double annualCityPaysForEmployeeIntoCalculatedPension = totalCityPaysForEmployeeIntoCalculatedPension / inputCareerYearsService;
        //System.out.println("(Pre-investment-gain) City input (in today's dollars) over a career (hired to retired) of changed pension: $" + (int) totalCityPaysForEmployeeIntoCalculatedPension);
        System.out.println(" "); //blank line
        System.out.println("Present Value of total City Contributions (over the minimum years of service, and not including employee contributions) until eligibility: $" + (int)totalCityPaysForEmployeeIntoCalculatedPension + ".");
        System.out.println(" "); //blank line

        //System.out.println("Approx Additional Cost to city of restoring benefits vs unchanged for this employee= $" + (int) (totalCityPaysForEmployeeIntoCalculatedPension - totalCityPaysForEmployeeIntoUnchangedPension) / inputCareerYearsService + " per year. ");
        //System.out.println("Approx amount to the city has \"saved\" by post-'96 downgraded benefits since hire date = $" + (int) (totalCityPaysForEmployeeIntoCalculatedPension - totalCityPaysForEmployeeIntoUnchangedPension) / inputCareerYearsService + " annually for " + (inputCurrentAge - (inputRetAge - inputCareerYearsService)) + " years. ");
        //System.out.println(" "); //blank line
        //System.out.println(" "); //blank line
        System.out.println(" "); //blank line

        System.out.print("Press Enter to quit...");
         sc.nextLine();
         sc.close();
         System.out.println("Goodbye!");
         System.exit(0);


    }


}
