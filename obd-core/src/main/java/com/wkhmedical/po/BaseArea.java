/**
 * 
 */
package com.wkhmedical.po;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 */

@Getter
@Setter
@Entity
@Table(name = "base_area")
@DynamicInsert
@DynamicUpdate
public class BaseArea implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	private Long pid;

	private Integer level;

	private String name;

}
