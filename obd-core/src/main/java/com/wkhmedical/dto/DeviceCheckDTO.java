package com.wkhmedical.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceCheckDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String eid;

	private Long bcabnm;

	private Long bcexam;

	private Long bioabnm;

	private Long bioexam;

	private Long bscanabnm;

	private Long bscanexam;

	private Long ecgabnm;

	private Long ecgexam;

	private Long ibpabnm;

	private Long ibpexam;

	private Long report;

	private Long urineabnm;

	private Long urineexam;

	private Long persontime;

	private String t0;

	private String t1;
}
