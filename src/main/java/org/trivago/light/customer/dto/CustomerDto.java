package org.trivago.light.customer.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@Builder
public class CustomerDto {

    @NotEmpty
    @Length(max = 140)
    @Email
    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String country;

    private String city;

}
