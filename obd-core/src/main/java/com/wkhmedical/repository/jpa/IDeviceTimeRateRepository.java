package com.wkhmedical.repository.jpa;

import java.math.BigDecimal;
import java.util.Date;

public interface IDeviceTimeRateRepository {

	BigDecimal getRateSumByDate(Date dt1, Date dt2);
}
