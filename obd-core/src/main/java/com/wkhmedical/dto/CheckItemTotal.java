/**
 * 
 */
package com.wkhmedical.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */
@Getter
@Setter
public class CheckItemTotal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long bcabnm;

	private Long bcexam;

	private Long bioabnm;

	private Long bioexam;

	private Long bscanabnm;

	private Long bscanexam;

	private Long ecgabnm;

	private Long ecgexam;

	private Long bpabnm;

	private Long bpexam;

	private Long report;

	private Long urineabnm;

	private Long urineexam;

	private Long persontime;

	private Long healthexam;

	private Long tcmexam;

}
