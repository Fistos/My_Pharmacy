package the31st.my_pharmacy;

/**
 * Created by TIH on 3/16/2017.
 */

public class Details {

    String prescription, patient_name, patient_sickness, patient_stage_sickness, patient_dr_name, patient_dr_email
            ,patient_address, kin_name, kin_email, kin_number;

    public Details() {
    }

    public Details(String prescription, String patient_name, String patient_sickness, String patient_stage_sickness, String patient_dr_name, String patient_dr_email, String patient_address, String kin_name, String kin_email, String kin_number) {
        this.prescription = prescription;
        this.patient_name = patient_name;
        this.patient_sickness = patient_sickness;
        this.patient_stage_sickness = patient_stage_sickness;
        this.patient_dr_name = patient_dr_name;
        this.patient_dr_email = patient_dr_email;
        this.patient_address = patient_address;
        this.kin_name = kin_name;
        this.kin_email = kin_email;
        this.kin_number = kin_number;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatient_sickness() {
        return patient_sickness;
    }

    public void setPatient_sickness(String patient_sickness) {
        this.patient_sickness = patient_sickness;
    }

    public String getPatient_stage_sickness() {
        return patient_stage_sickness;
    }

    public void setPatient_stage_sickness(String patient_stage_sickness) {
        this.patient_stage_sickness = patient_stage_sickness;
    }

    public String getPatient_dr_name() {
        return patient_dr_name;
    }

    public void setPatient_dr_name(String patient_dr_name) {
        this.patient_dr_name = patient_dr_name;
    }

    public String getPatient_dr_email() {
        return patient_dr_email;
    }

    public void setPatient_dr_email(String patient_dr_email) {
        this.patient_dr_email = patient_dr_email;
    }

    public String getPatient_address() {
        return patient_address;
    }

    public void setPatient_address(String patient_address) {
        this.patient_address = patient_address;
    }

    public String getKin_name() {
        return kin_name;
    }

    public void setKin_name(String kin_name) {
        this.kin_name = kin_name;
    }

    public String getKin_email() {
        return kin_email;
    }

    public void setKin_email(String kin_email) {
        this.kin_email = kin_email;
    }

    public String getKin_number() {
        return kin_number;
    }

    public void setKin_number(String kin_number) {
        this.kin_number = kin_number;
    }
}
