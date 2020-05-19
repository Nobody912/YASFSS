public class ap2 {
    private final double grade;

    public ap2(double gradeVal)
    {
        this.grade = gradeVal;
    }
    public static double getGrade()
    {
        return -1.0;
    }
    
    public static double gradeSimulation()
    {
        double total = 0.0;
        double count = 0.0;
        double temp = getGrade();
        while (temp >= 0.0)
        {
            if (temp >= 90.0)
            {
                count++;
            }
            total++;
        }
        return count / total;
    }
}