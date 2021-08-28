package com.github.norwick.reciperodeo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.ContactMessage;
import com.github.norwick.reciperodeo.repository.ContactMessageRepository;

@Service
public class ContactMessageService {
	@Autowired
	ContactMessageRepository contactMessageRepository;
	
	/**
	 * Saves a contact message
	 * @param cm the contact message to save
	 * @return the database representation of the contact message
	 */
	public ContactMessage saveContactMessage(ContactMessage cm) {
		return contactMessageRepository.save(cm);
	}
	
}
