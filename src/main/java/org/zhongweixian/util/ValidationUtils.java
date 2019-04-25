package org.zhongweixian.util;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;
import org.zhongweixian.exception.ErrorCode;
import org.zhongweixian.exception.PayException;


/**
 * Created by caoliang on  6/6/2018
 */
public class ValidationUtils {

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();

    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new PayException(ErrorCode.XML_PARSE_ERROR, constraintViolations.iterator().next().getMessage());
        }
    }

}
