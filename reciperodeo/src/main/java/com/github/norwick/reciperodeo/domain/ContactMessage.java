package com.github.norwick.reciperodeo.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Norwick Lee
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTimestamp;
	
	@NotNull
	@Size(min=1)
	String name;
	
	@NotNull
	@Email
	String email;
	
	@Size(max=100)
	String subject;
	
	@NotNull
	@Lob
	String message;
}
