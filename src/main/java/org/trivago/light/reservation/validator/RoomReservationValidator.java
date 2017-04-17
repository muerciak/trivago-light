package org.trivago.light.reservation.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.trivago.light.reservation.dto.RoomReservationDto;

@Component
public class RoomReservationValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return RoomReservationDto.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "dateFrom", "RoowReservationDto.dateFromEmpty");
        ValidationUtils.rejectIfEmpty(errors, "dateTo", "RoowReservationDto.dateToEmpty");
        RoomReservationDto dto = (RoomReservationDto) o;
        if (!errors.hasFieldErrors() && dto.getDateTo().isBefore(dto.getDateFrom())) {
            errors.rejectValue("dateFrom", "RoowReservationDto.dateToIsBeforeDateFrom", null, "");
        }
        if (!errors.hasFieldErrors() && dto.getDateTo().isEqual(dto.getDateFrom())) {
            errors.rejectValue("dateFrom", "RoowReservationDto.dateToIsEqualsDateFrom", null, "");
        }
    }
}
