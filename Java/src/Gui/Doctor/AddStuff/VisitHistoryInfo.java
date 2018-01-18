package Gui.Doctor.AddStuff;

public class VisitHistoryInfo {
    private String date, disease, advice, medicine;

    public VisitHistoryInfo(String date, String disease, String advice, String medicine) {
        this.date = date;
        this.disease = disease;
        this.advice = advice;
        this.medicine = medicine;
    }

    public String getDisease() {
        return disease;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getDate() {
        return date;
    }

    public String getAdvice() {
        return advice;
    }
}
