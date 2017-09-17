package com.sling.tv.core.models;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.date.DateUtil;
import com.day.cq.commons.date.InvalidDateException;

/**
 * This Sling Model is for Countdown Clock Component.
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class CountdownClockModel {

	/** The Constant LOGGER. */
	private static final Logger	LOGGER	= LoggerFactory.getLogger(CountdownClockModel.class);

	/** The countdown start date. */
	@Inject
	@Optional
	@Via("resource")
	private String				countdownStartDate;

	/** The countdown end text. */
	@Inject
	@Optional
	@Via("resource")
	private String				countdownEndText;

	/** The date. */
	private Date				date	= null;

	/** The cal. */
	private Calendar			cal		= null;

	/**
	 * Inits the.
	 */
	@PostConstruct
	private void init() {

	}

	/**
	 * Gets the countdown start formated date.
	 *
	 * @return the countdown start formated date
	 * @throws InvalidDateException the invalid date exception
	 */
	public String getCountdownStartFormatedDate() throws InvalidDateException {
		this.cal = DateUtil.parseISO8601(this.countdownStartDate);
		this.date = this.cal.getTime();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(this.date);
	}

	/**
	 * Gets the day text.
	 *
	 * @return the day text
	 */
	public String getDayText() {
		final String weekdays[] = new DateFormatSymbols().getWeekdays();
		return weekdays[this.cal.get(Calendar.DAY_OF_WEEK)];
	}

	/**
	 * Gets the month text.
	 *
	 * @return the month text
	 */
	public String getMonthText() {
		final String months[] = new DateFormatSymbols().getMonths();
		return months[this.cal.get(Calendar.MONTH)];
	}

	/**
	 * Gets the day.
	 *
	 * @return the day
	 */
	public int getDay() {
		return this.cal.get(Calendar.DAY_OF_MONTH);

	}

	/**
	 * Gets the hour month.
	 *
	 * @return the hour month
	 */
	public String getHourMonth() {
		final SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a zzz");
		return formatDate.format(this.date);
	}

}
