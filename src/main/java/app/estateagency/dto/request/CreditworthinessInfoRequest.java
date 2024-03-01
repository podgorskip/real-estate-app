package app.estateagency.dto.request;

import app.estateagency.enums.credit.EducationLevel;
import app.estateagency.enums.credit.EmploymentStatus;
import app.estateagency.enums.credit.Gender;
import app.estateagency.enums.credit.MaritalStatus;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreditworthinessInfoRequest {
    @NotNull(message = "Age cannot be null")
    @Min(value = 18, message = "User must be at least 18 years old")
    private Integer age;

    @NotNull(message = "Marital status cannot be null")
    private String maritalStatus;

    @NotNull(message = "Employment status cannot be null")
    private String employmentStatus;

    @NotNull(message = "Income cannot be null")
    @Positive(message = "Income must be positive")
    private Double income;

    @NotNull(message = "Debts cannot be null")
    @Min(value = 0, message = "Debts must be greater or equal 0")
    private Double debts;

    @NotNull(message = "Education level cannot be null")
    private String educationLevel;

    @NotNull(message = "Gender cannot be null")
    private String gender;

    @NotNull(message = "Expenses cannot be null")
    private Double expenses;
}
